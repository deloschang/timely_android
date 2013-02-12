package com.example.timely;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

public class LocationService extends Service {
	private LocationManager lm;
	
	public void onStartCommand(Intent intent, int startId){
		super.onStartCommand(intent, startId);
		addLocationListener();
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
	
	
}