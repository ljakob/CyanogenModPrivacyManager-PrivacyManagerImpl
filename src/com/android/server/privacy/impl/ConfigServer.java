package com.android.server.privacy.impl;

import android.app.ActivityManagerNative;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;

import com.android.server.privacy.impl.model.ConfigModel;
import com.android.server.privacy.impl.model.PackageConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigServer extends IPrivacyManagerConfig.Stub {
	private static final String TAG = PrivacyManagerImpl.class.getSimpleName();

	private Context m_context;
	private ConfigModel m_cfg;
	private File m_modelFile;
	private Set<String> m_revokeablePermissions;

	public ConfigServer(Context context, ConfigModel cfg, File modelFile, Set<String> revokeablePermissions) {
		m_context = context;
		m_cfg = cfg;
		m_modelFile = modelFile;
		m_revokeablePermissions = revokeablePermissions;
	}

	@Override
	public List<String> getConfiguredPackages() throws RemoteException {
		synchronized (m_cfg) {
			return m_cfg.getPackagesNames();
		}
	}

	@Override
	public List<String> getRevokedPermissions(String packageName) throws RemoteException {
		if (packageName == null)
			throw new IllegalArgumentException();
		synchronized (m_cfg) {
			PackageConfig p = m_cfg.getPackageConfig(packageName);
			if (p == null)
				return null; // unknown
			return p.getRevokedPermissions();
		}
	}

	@Override
	public void setRevokedPermissions(String packageName, List<String> permissions) throws RemoteException {
		if (packageName == null || permissions == null)
			throw new IllegalArgumentException();
		
		for (String p : permissions) {
			if ( !m_revokeablePermissions.contains(p) ) throw new IllegalArgumentException(p + " is not revokable");
		}
		
		synchronized (m_cfg) {
			PackageConfig p = m_cfg.getPackageConfig(packageName);
			if (p == null) {
				p = new PackageConfig(packageName);
				m_cfg.put(p);
			}
			p.setRevokedPermissions(permissions);
			save();
		}
		
		ApplicationInfo ai;
		try {
			ai = m_context.getPackageManager().getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// ignore
			return;
		}
		
		IActivityManager am = ActivityManagerNative.getDefault();
		Binder.clearCallingIdentity(); // ignore old identity
		int appId = UserHandle.getAppId(AppGlobals.getPackageManager().getPackageUid(packageName, 0));
		am.killApplicationWithAppId(packageName, appId);
		Slog.d(TAG, "killed " + packageName);
	}

	@Override
	public void removePackage(String packageName) throws RemoteException {
		synchronized (m_cfg) {
			m_cfg.removePackage(packageName);
			save();
		}
	}

	private void save() throws RemoteException {
		try {
			m_cfg.saveXML(m_modelFile);
			Slog.d(TAG, "saved to " + m_modelFile);
		} catch (Exception e) {
			throw new RuntimeException("can't save model", e);
		}
	}

	@Override
	public List<String> getRevokeablePermissions() throws RemoteException {
		return new ArrayList<String>(m_revokeablePermissions);
	}

}
