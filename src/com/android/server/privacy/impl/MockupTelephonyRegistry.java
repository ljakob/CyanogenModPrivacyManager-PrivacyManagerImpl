package com.android.server.privacy.impl;

import android.net.LinkCapabilities;
import android.net.LinkProperties;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.CellInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;

import com.android.internal.telephony.IPhoneStateListener;

import java.util.List;

public class MockupTelephonyRegistry extends com.android.internal.telephony.ITelephonyRegistry.Stub {

	public MockupTelephonyRegistry() {
		
	}
	
	@Override
	public void listen(String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
		// ignore
	}

	@Override
	public void notifyCallState(int state, String incomingNumber) throws RemoteException {
	}

	@Override
	public void notifyServiceState(ServiceState state) throws RemoteException {
	}

	@Override
	public void notifySignalStrength(SignalStrength signalStrength) throws RemoteException {
	}

	@Override
	public void notifyMessageWaitingChanged(boolean mwi) throws RemoteException {
	}

	@Override
	public void notifyCallForwardingChanged(boolean cfi) throws RemoteException {
	}

	@Override
	public void notifyDataActivity(int state) throws RemoteException {
	}

	@Override
	public void notifyDataConnection(int state, boolean isDataConnectivityPossible, String reason, String apn, String apnType, LinkProperties linkProperties, LinkCapabilities linkCapabilities,
			int networkType, boolean roaming) throws RemoteException {
	}

	@Override
	public void notifyDataConnectionFailed(String reason, String apnType) throws RemoteException {
	}

	@Override
	public void notifyCellLocation(Bundle cellLocation) throws RemoteException {
	}

	@Override
	public void notifyOtaspChanged(int otaspMode) throws RemoteException {
	}

    @Override
    public void notifyCellInfo(List<CellInfo> cellInfo) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

}
