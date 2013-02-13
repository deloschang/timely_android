package com.example.timely;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
	
	double latitude, longitude;
	
	@Override
	public void onReceive(Context context, Intent calledIntent) {
		// TODO Auto-generated method stub
		Log.d("LOC_RECEIVER", "Location RECEIVED!");
		
		latitude = calledIntent.getDoubleExtra("latitude", -1);
		longitude = calledIntent.getDoubleExtra("longitude", -1);
		
		updateRemote(latitude, longitude);
	}
	
	private void updateRemote(final double latitude, final double longitude){
		// SEND UPDATE TO THE SERVER VIA POST
	}
}