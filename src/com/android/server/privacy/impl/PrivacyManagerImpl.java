package com.android.server.privacy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.util.Slog;

import com.android.server.privacy.PrivacyManagerSPI;
import com.android.server.privacy.impl.model.ConfigModel;
import com.android.server.privacy.impl.model.PackageConfig;

public class PrivacyManagerImpl implements PrivacyManagerSPI {
	/* package */ static final String TAG = PrivacyManagerImpl.class.getSimpleName();

	private static final File MODEL_FILE = new File("/data/system/PrivacyManager.xml");
	
	private Context m_context;
    private Map<String, Map<String,IBinder>> m_Mockups = new HashMap<String, Map<String,IBinder>>(); // service, permission, impl
    private Set<String> m_revokeablePermissions = new HashSet<String>();
    private ConfigModel m_cfg = new ConfigModel();
	private final ConfigServer m_configAgent;

	private MockupPackageManager m_pm;

	private MockupActivityManager m_am;

	private void addMockup(String serviceName, IBinder mockupImpl, String... permissions) {
		for(String p : permissions) {
			m_revokeablePermissions.add(p);
			Map<String, IBinder> m = m_Mockups.get(serviceName);
			if ( m == null ) {
				m = new HashMap<String, IBinder>();
				m_Mockups.put(serviceName, m);
			}
			m.put(p, mockupImpl);
		}
	}
	
	public PrivacyManagerImpl(Context context) {
		m_context = context;
		
		addMockup(Context.VIBRATOR_SERVICE, new MockupVibrator(context), android.Manifest.permission.VIBRATE);
		addMockup(Context.LOCATION_SERVICE, new MockupLocationServer(context), android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);
		addMockup("iphonesubinfo", new MockupPhoneSubInfo(context), android.Manifest.permission.READ_PHONE_STATE);
		addMockup("telephony.registry", new MockupTelephonyRegistry(), android.Manifest.permission.READ_PHONE_STATE);
		addMockup(Context.CONNECTIVITY_SERVICE, new MockupConnectivityManager(context), android.Manifest.permission.INTERNET);
		addMockup(Context.WIFI_SERVICE, new MockupWifi(), android.Manifest.permission.ACCESS_WIFI_STATE);
		addMockup(Context.ACCOUNT_SERVICE, new MockupAccount(), android.Manifest.permission.GET_ACCOUNTS, android.Manifest.permission.MANAGE_ACCOUNTS);
		m_revokeablePermissions.add(android.Manifest.permission.READ_CONTACTS);
		m_revokeablePermissions.add(android.Manifest.permission.WRITE_CONTACTS);
		m_revokeablePermissions.add(android.Manifest.permission.READ_CALENDAR);
		m_revokeablePermissions.add(android.Manifest.permission.WRITE_CALENDAR);
		m_revokeablePermissions.add(android.Manifest.permission.RECEIVE_BOOT_COMPLETED);
		m_revokeablePermissions.add(android.Manifest.permission.USE_CREDENTIALS);
		m_revokeablePermissions.add(android.Manifest.permission.CHANGE_WIFI_STATE);
		m_revokeablePermissions.add(android.Manifest.permission.READ_CALL_LOG);
		m_revokeablePermissions.add("com.android.vending.CHECK_LICENSE");
		m_revokeablePermissions.add("com.android.vending.BILLING");
				
		if ( MODEL_FILE.canRead() ) {
			try {
				m_cfg = ConfigModel.loadXML(MODEL_FILE);
			} catch (Exception e) {
				Slog.e(TAG, "can't load model file", e);
			}
		}
		
		m_configAgent = new ConfigServer(m_context, m_cfg, MODEL_FILE, m_revokeablePermissions);
		m_pm = new MockupPackageManager(context, m_cfg);
		m_am = new MockupActivityManager(context, m_pm, m_cfg);
	}

	@Override
	public String toString() {
		return "hello world19";
	}

	@Override
	public IBinder getPrivacySubstituteService(String service, String packageName) {
//		Slog.d(TAG, "caller=" + packageName + " service=" + service);
		
		PackageConfig cfg = m_cfg.getPackageConfig(packageName);
		if ( cfg == null || cfg.getRevokedPermissions().isEmpty() ) return null; // no config
		
		if ( service.equals("package") ) {
			Slog.d(TAG, "new mockup PackageManager to " + packageName);
			return m_pm;
		}

		if ( service.equals("activity") ) {
			Slog.d(TAG, "new mockup ActivityManager to " + packageName);
			return m_am;
		}

		Map<String, IBinder> m = m_Mockups.get(service);
		if ( m == null ) return null; // service not revokeable
		
		
		for(String p : cfg.getRevokedPermissions() ) {
			IBinder res = m.get(p);
			if ( res != null ) {
				Slog.d(TAG, "new mockup " + res + " for revoked permission " + p);
				return res;
			}
		}
		return null; // not revoked
	}

	@Override
	public PackageInfo filterPackageInfo(PackageInfo info, String packageName, int flags) {
		if ( info.gids != null ) {
			info.gids = filterPackageGids(info.gids, packageName);
		}
		return info;
	}

	@Override
	public int[] filterPackageGids(int[] gids, String packageName) {
//		if ( true ) return gids;
		
		if ( gids == null ) return null;
		
		PackageConfig cfg = m_cfg.getPackageConfig(packageName);
		if ( cfg == null ) return gids;
		
		boolean remove_3003 = cfg.isPermissionRevoked(Manifest.permission.INTERNET);
		if ( !(remove_3003) ) return gids;
		
		// we need a copy because gids is within the PackageManager - don't mess up with that
		{
			int[] g = new int[gids.length];
			System.arraycopy(gids, 0, g, 0, g.length);
			gids = g;
		}
		
		int count = gids.length; // returned items
		for(int i=0;i<gids.length;i++) {
			int g = gids[i];
			if (  g == 3003 && remove_3003 ) { // inet
				gids[i] = -1; // mark as bad
				count--;
			}
		}
		
		// frameworks/base/core/java/android/os/Process.java
		// ./system/core/include/private/android_filesystem_config.h
		// ./packages/apps/Superuser/src/com/noshufou/android/su/util/Util.java
		
		if ( gids.length != count ) {
			int[] n = new int[count];
			count=0;
			for(int g:gids) {
				if ( g < 0 ) continue;
				n[count++] = g;
			}
			gids = n;
			Slog.d(TAG, "revoked guids " + packageName);
		}
		if ( gids.length == 0 ) return null; // empty array is error!
		return gids; // nothing
	}

	@Override
	public boolean filterGrantedPermission(String permName, String pkgName) {
//		Slog.d(TAG, "filterGrantedPermission2 permName=" + permName + " pkgName=" + pkgName);
		
		PackageConfig cfg = m_cfg.getPackageConfig(pkgName);
		if ( cfg == null ) return true; // granted
		if ( cfg.getRevokedPermissions().contains(permName) ) return false; // deny
		
		return true;
	}

	@Override
	public IBinder getConfig() {
		return m_configAgent;
	}
}
