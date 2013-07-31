package com.apps.kifall;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
 
public class GPSTracker extends Service implements LocationListener {
 
    private final Context mContext;
 
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    
    // The minimum distance to change Updates in meters
 	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 	// The minimum time between updates in milliseconds
 	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
    
    
    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }
    
    public void verifyIfGPSEnabled(){
    	Log.d("TAG", "verify if location enalbed");
    	 try {
    	    	Log.d("TAG", "verify if location enalbed");

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
 	    	Log.d("TAG", "verify if location enalbed");

  
             // getting GPS status
             isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  	    	Log.d("TAG", "verify if location enalbed");

             // getting network status
             isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


             if (isGPSEnabled || isNetworkEnabled)
                 this.canGetLocation = true;
             
    	  } catch (Exception e) {
              e.printStackTrace();
          }
    }
 
    //getLocation using the network provider and the gps proivder if enabled
    public Location getLocation() {
    	try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                 
                    
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
    	}
            catch (Exception e) {
                e.printStackTrace();
            }
            

        return location;
    }

    // Function to get latitud
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
         
        // return latitude
        return latitude;
    }
     

    // Function to get longitude
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }
    
     
    // Function to check GPS/wifi enabled
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

	@Override
	public void onLocationChanged(android.location.Location location) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
 
}
