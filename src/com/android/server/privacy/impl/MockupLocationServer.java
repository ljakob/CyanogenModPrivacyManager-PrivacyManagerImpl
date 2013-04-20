package com.android.server.privacy.impl;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.GeocoderParams;
import android.location.Geofence;
import android.location.IGpsStatusListener;
import android.location.ILocationListener;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.internal.location.ProviderProperties;

import java.util.Collections;
import java.util.List;

public class MockupLocationServer extends android.location.ILocationManager.Stub {

	private static final String DUMMY = "Magic Provider";
	private static final Location FIXED_LOCATION = new Location(DUMMY);
	static {
		FIXED_LOCATION.setLatitude(0);
		FIXED_LOCATION.setLongitude(0);
		FIXED_LOCATION.setAccuracy(30);
	}		
	
	public MockupLocationServer(Context context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<String> getAllProviders() throws RemoteException {
		return Collections.singletonList(DUMMY);
	}

	@Override
	public List<String> getProviders(Criteria criteria, boolean enabledOnly) throws RemoteException {
		return Collections.emptyList();
	}

	@Override
	public String getBestProvider(Criteria criteria, boolean enabledOnly) throws RemoteException {
		return DUMMY;
	}

	@Override
	public boolean providerMeetsCriteria(String provider, Criteria criteria) throws RemoteException {
		return true;
	}

	@Override
	public boolean addGpsStatusListener(IGpsStatusListener listener) throws RemoteException {
		return false;
	}

	@Override
	public void removeGpsStatusListener(IGpsStatusListener listener) throws RemoteException {
	}

	@Override
	public void locationCallbackFinished(ILocationListener listener) throws RemoteException {
	}

	@Override
	public boolean sendExtraCommand(String provider, String command, Bundle extras) throws RemoteException {
		return false;
	}

	@Override
	public boolean isProviderEnabled(String provider) throws RemoteException {
		return DUMMY.equals(provider);
	}

	@Override
	public void reportLocation(Location location, boolean passive) throws RemoteException {
	}

	@Override
	public boolean geocoderIsPresent() throws RemoteException {
		return false;
	}

	@Override
	public String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
		return "nowhere";
	}

	@Override
	public String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults,
			GeocoderParams params, List<Address> addrs) throws RemoteException {
		return "nowhere";
	}

	@Override
	public void removeTestProvider(String provider) throws RemoteException {
	}

	@Override
	public void setTestProviderLocation(String provider, Location loc) throws RemoteException {
	}

	@Override
	public void clearTestProviderLocation(String provider) throws RemoteException {
	}

	@Override
	public void setTestProviderEnabled(String provider, boolean enabled) throws RemoteException {
	}

	@Override
	public void clearTestProviderEnabled(String provider) throws RemoteException {
	}

	@Override
	public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime) throws RemoteException {
	}

	@Override
	public void clearTestProviderStatus(String provider) throws RemoteException {
	}

	@Override
	public boolean sendNiResponse(int notifId, int userResponse) throws RemoteException {
		return false;
	}

    @Override
    public void requestLocationUpdates(LocationRequest request, ILocationListener listener,
            PendingIntent intent, String packageName) throws RemoteException {
    }

    @Override
    public void removeUpdates(ILocationListener listener, PendingIntent intent, String packageName)
            throws RemoteException {
    }

    @Override
    public void requestGeofence(LocationRequest request, Geofence geofence, PendingIntent intent,
            String packageName) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeGeofence(Geofence fence, PendingIntent intent, String packageName)
            throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Location getLastLocation(LocationRequest request, String packageName)
            throws RemoteException {
        return FIXED_LOCATION;
    }

    @Override
    public ProviderProperties getProviderProperties(String provider) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addTestProvider(String name, ProviderProperties properties) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

}
