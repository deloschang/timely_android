package com.example.timely;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class LocationService extends Service {
	private LocationManager lm;
	private MyLocationListener MyLocationListener;
	
    // Shared preferences
    public static SharedPreferences settings;
    public static SharedPreferences.Editor configEditor;
    
	public void onStartCommand(Intent intent, int startId){
		super.onStartCommand(intent, startId, startId); // flags parameter?
		
		// set locationService preference to true
		configEditor.putBoolean("locationService", true).commit();
		
		addLocationListener();
	}
	
	public void onDestroy(){
		// set locationService preference to false
		configEditor.putBoolean("locationService", false).commit();
	}
	
	private void addLocationListener(){
		Thread triggerServ = new Thread(new Runnable(){
			public void run(){
				try {
					Looper.prepare(); // Initialize current thread as a looper
					lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
					
					Criteria c = new Criteria();
					c.setAccuracy(Criteria.ACCURACY_COARSE);
					
					final String PROVIDER = lm.getBestProvider(c, true);
					
					MyLocationListener = new MyLocationListener();
					lm.requestLocationUpdates(PROVIDER, 600000, 0, MyLocationListener);
					
					Log.d("LOC_SERVICE", "Service RUNNING!");
					Looper.loop();
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}, "LocationThread");
		triggerServ.start();
	}
	
	public static void updateLocation(Location location){
		Context appCtx = MainExt.getAppContext();
		
		double latitude, longitude;
		
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
		Intent filterRes = new Intent();
		filterRes.setAction("android.intent.action.LOCATION");
		filterRes.putExtra("latitude", latitude);
		filterRes.putExtra("longitude", longitude);
		
		appCtx.sendBroadcast(filterRes);
	}
	
	public class MyLocationListener implements LocationListener { 
		
		@Override
		public void onLocationChanged(Location location){
			updateLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}