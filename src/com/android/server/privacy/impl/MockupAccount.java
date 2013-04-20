package com.android.server.privacy.impl;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import android.os.RemoteException;

public class MockupAccount extends android.accounts.IAccountManager.Stub {

	@Override
	public String getPassword(Account account) throws RemoteException {
		return null;
	}

	@Override
	public String getUserData(Account account, String key) throws RemoteException {
		return "unknown";
	}

	@Override
	public AuthenticatorDescription[] getAuthenticatorTypes() throws RemoteException {
		return new AuthenticatorDescription[] {};
	}

	@Override
	public Account[] getAccounts(String accountType) throws RemoteException {
		Account res = new Account("dev@null.org", "offline");
		return new Account[] { res };
	}

	@Override
	public void hasFeatures(IAccountManagerResponse response, Account account, String[] features) throws RemoteException {
		// ignore
	}

	@Override
	public void getAccountsByFeatures(IAccountManagerResponse response, String accountType, String[] features) throws RemoteException {
		// ignore
	}

	@Override
	public boolean addAccount(Account account, String password, Bundle extras) throws RemoteException {
		return false;
	}

	@Override
	public void removeAccount(IAccountManagerResponse response, Account account) throws RemoteException {
		// ignore
	}

	@Override
	public void invalidateAuthToken(String accountType, String authToken) throws RemoteException {
		// ignore
	}

	@Override
	public String peekAuthToken(Account account, String authTokenType) throws RemoteException {
		return null;
	}

	@Override
	public void setAuthToken(Account account, String authTokenType, String authToken) throws RemoteException {
		// ignore
	}

	@Override
	public void setPassword(Account account, String password) throws RemoteException {
		// ignore
	}

	@Override
	public void clearPassword(Account account) throws RemoteException {
		// ignore
	}

	@Override
	public void setUserData(Account account, String key, String value) throws RemoteException {
		// ignore
	}

	@Override
	public void getAuthToken(IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle options) throws RemoteException {
		// ignore
	}

	@Override
	public void addAcount(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
		// ignore
	}

	@Override
	public void updateCredentials(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
		// ignore
	}

	@Override
	public void editProperties(IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
		// ignore
	}

    @Override
    public Account[] getAccountsAsUser(String accountType, int userId) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateAppPermission(Account account, String authTokenType, int uid, boolean value)
            throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void confirmCredentialsAsUser(IAccountManagerResponse response, Account account,
            Bundle options, boolean expectActivityLaunch, int userId) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getAuthTokenLabel(IAccountManagerResponse response, String accountType,
            String authTokenType) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
}
