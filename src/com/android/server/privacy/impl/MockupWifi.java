package com.android.server.privacy.impl;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import android.net.DhcpInfo;
import android.net.wifi.IWifiManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Slog;

/**
 * @hide
 */
class MockupWifi extends IWifiManager.Stub {

	private static final String TAG = MockupWifi.class.getSimpleName();

	@Override
	public List<WifiConfiguration> getConfiguredNetworks() throws RemoteException {
		return Collections.emptyList();
	}

	@Override
	public int addOrUpdateNetwork(WifiConfiguration config) throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public boolean removeNetwork(int netId) throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public boolean enableNetwork(int netId, boolean disableOthers) throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public boolean disableNetwork(int netId) throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public boolean pingSupplicant() throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public void startScan(boolean forceActive) throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public List<ScanResult> getScanResults() throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public void disconnect() throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public void reconnect() throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public void reassociate() throws RemoteException {
		throw new SecurityException("not implemented");
	}

	@Override
	public WifiInfo getConnectionInfo() throws RemoteException {
		try {
			WifiInfo wifi = new WifiInfo(null);
			
			Method setMac = wifi.getClass().getDeclaredMethod("setMacAddress", String.class);
			setMac.setAccessible(true);
			setMac.invoke(wifi, "AA:BB:CC:DD:EE:FF");
			
			Method setState = wifi.getClass().getDeclaredMethod("setSupplicantState", SupplicantState.class);
			setState.setAccessible(true);
			setState.invoke(wifi, SupplicantState.DISCONNECTED);
			
			return wifi;
		} catch (Throwable t) {
			Slog.e(TAG, "return wifi info", t);
			return null;
		}
	}

	@Override
	public boolean setWifiEnabled(boolean enable) throws RemoteException {
		return false;
	}

	@Override
	public int getWifiEnabledState() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCountryCode(String country, boolean persist) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFrequencyBand(int band, boolean persist) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFrequencyBand() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDualBandSupported() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveConfiguration() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DhcpInfo getDhcpInfo() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateWifiLockWorkSource(IBinder lock, WorkSource ws) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean releaseWifiLock(IBinder lock) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initializeMulticastFiltering() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMulticastEnabled() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void acquireMulticastLock(IBinder binder, String tag) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void releaseMulticastLock() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWifiApEnabled(WifiConfiguration wifiConfig, boolean enable) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWifiApEnabledState() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WifiConfiguration getWifiApConfiguration() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWifiApConfiguration(WifiConfiguration wifiConfig) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startWifi() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopWifi() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addToBlacklist(String bssid) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBlacklist() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getConfigFile() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String getCountryCode() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Messenger getWifiServiceMessenger() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Messenger getWifiStateMachineMessenger() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void captivePortalCheckComplete() throws RemoteException {
        // TODO Auto-generated method stub
        
    }

}