package com.android.server.privacy.impl;

import android.content.Context;
import android.os.RemoteException;

import com.android.internal.telephony.IPhoneSubInfo;

public class MockupPhoneSubInfo extends IPhoneSubInfo.Stub {

	private static final String DUMMY_NUMBER = "123";

	public MockupPhoneSubInfo(Context context) {
	}

	@Override
	public String getDeviceId() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getDeviceSvn() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getSubscriberId() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getIccSerialNumber() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getLine1Number() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getLine1AlphaTag() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getMsisdn() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getVoiceMailNumber() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getCompleteVoiceMailNumber() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getVoiceMailAlphaTag() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getIsimImpi() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String getIsimDomain() throws RemoteException {
		return DUMMY_NUMBER;
	}

	@Override
	public String[] getIsimImpu() throws RemoteException {
		return new String[] { DUMMY_NUMBER };
	}

}
