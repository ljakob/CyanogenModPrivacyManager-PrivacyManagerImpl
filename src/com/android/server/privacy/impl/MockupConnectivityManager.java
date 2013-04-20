package com.android.server.privacy.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkQuotaInfo;
import android.net.NetworkState;
import android.net.ProxyProperties;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Slog;

import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;

public class MockupConnectivityManager extends android.net.IConnectivityManager.Stub {

	private static final String TAG = MockupConnectivityManager.class.getSimpleName();
	
	public MockupConnectivityManager(Context context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setNetworkPreference(int pref) throws RemoteException {
	}

	@Override
	public int getNetworkPreference() throws RemoteException {
		return ConnectivityManager.TYPE_WIFI;
	}

	@Override
	public NetworkInfo getActiveNetworkInfo() throws RemoteException {
		NetworkInfo res = new NetworkInfo(ConnectivityManager.TYPE_WIFI, 0, "NONE", "");
		res.setIsAvailable(false);
		Slog.d(TAG, "fake offline");
		return res;
	}

	@Override
	public NetworkInfo getActiveNetworkInfoForUid(int uid) throws RemoteException {
		return getActiveNetworkInfo();
	}

	@Override
	public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
		return getActiveNetworkInfo();
	}

	@Override
	public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
		return new NetworkInfo[] { getActiveNetworkInfo() };
	}

	@Override
	public boolean isNetworkSupported(int networkType) throws RemoteException {
		return false;
	}

	@Override
	public LinkProperties getActiveLinkProperties() throws RemoteException {
		return null;
	}

	@Override
	public LinkProperties getLinkProperties(int networkType) throws RemoteException {
		return null;
	}

	@Override
	public NetworkState[] getAllNetworkState() throws RemoteException {
		return null;
	}

	@Override
	public NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException {
		return null;
	}

	@Override
	public boolean setRadios(boolean onOff) throws RemoteException {
		return false;
	}

	@Override
	public boolean setRadio(int networkType, boolean turnOn) throws RemoteException {
		return false;
	}

	@Override
	public int startUsingNetworkFeature(int networkType, String feature, IBinder binder) throws RemoteException {
		return 0;
	}

	@Override
	public int stopUsingNetworkFeature(int networkType, String feature) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean requestRouteToHost(int networkType, int hostAddress) throws RemoteException {
		return false;
	}

	@Override
	public boolean requestRouteToHostAddress(int networkType, byte[] hostAddress) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMobileDataEnabled() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMobileDataEnabled(boolean enabled) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolicyDataEnable(int networkType, boolean enabled) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int tether(String iface) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int untether(String iface) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLastTetherError(String iface) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTetheringSupported() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getTetherableIfaces() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTetheredIfaces() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTetheredIfacePairs() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTetheringErroredIfaces() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTetherableUsbRegexs() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTetherableWifiRegexs() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTetherableBluetoothRegexs() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setUsbTethering(boolean enable) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void requestNetworkTransitionWakelock(String forWhom) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportInetCondition(int networkType, int percentage) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProxyProperties getGlobalProxy() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGlobalProxy(ProxyProperties p) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProxyProperties getProxy() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataDependency(int networkType, boolean met) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean protectVpn(ParcelFileDescriptor socket) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean prepareVpn(String oldPackage, String newPackage) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ParcelFileDescriptor establishVpn(VpnConfig config) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LegacyVpnInfo getLegacyVpnInfo() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public boolean isActiveNetworkMetered() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void startLegacyVpn(VpnProfile profile) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean updateLockdownVpn() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void captivePortalCheckComplete(NetworkInfo info) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

}
