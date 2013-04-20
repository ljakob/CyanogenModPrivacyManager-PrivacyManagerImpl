package com.android.server.privacy.impl;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.IVibratorService;
import android.os.RemoteException;
import android.util.Slog;
import android.widget.Toast;

/**
 * @hide
 */
class MockupVibrator extends IVibratorService.Stub {

	private static final String TAG = MockupVibrator.class.getSimpleName();
	
	private final Context m_context;
	
    public MockupVibrator(Context context) {
    	m_context = context;
	}

	@Override
    public boolean hasVibrator() throws RemoteException {
        return true;
    }

    @Override
    public void vibrate(long milliseconds, IBinder token) throws RemoteException {
        Slog.d(TAG, "fake vibrate1");
        long identity = Binder.clearCallingIdentity();
        try {
        	Toast.makeText(m_context, "fake vibrate", Toast.LENGTH_SHORT).show();
        } finally {
        	Binder.restoreCallingIdentity(identity);
        }
    }

    @Override
    public void vibratePattern(long[] pattern, int repeat, IBinder token)
            throws RemoteException {
        Slog.d(TAG, "fake vibrate1");
        long identity = Binder.clearCallingIdentity();
        try {
        	Toast.makeText(m_context, "fake vibrate", Toast.LENGTH_SHORT).show();
        } finally {
        	Binder.restoreCallingIdentity(identity);
        }
    }

    @Override
    public void cancelVibrate(IBinder token) throws RemoteException {
    	// ignore
    }

}