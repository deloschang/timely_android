package com.example.timely;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;


// deprecate later
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.timely.MESSAGE";
	
	private TextView mLatLng;
	private LocationManager mLocationManager;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// We should restore app state (if exists) after rotation
		//
		//
		
		mLatLng = (TextView) findViewById(R.id.latlng);
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	}
	
	
	@Override
	protected void onStart(){
		super.onStart();
		
		// Need to check if GPS setting is enabled on device.
        // This verification should be done during onStart() because the system calls this method
        // when the user returns to the activity, which ensures the desired location provider is
        // enabled each time the activity resumes from the stopped state.
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if (!gpsEnabled){
			// build an alert dialog here that requests the user to enable the location service
			
		}

	}
	
	@Override
	protected void onStop(){
		super.onStop();
		mLocationManager.removeUpdates(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// Called when user clicks button
//	public void sendMessage(View view){
//		Intent intent = new Intent(this, DisplayMessageActivity.class);
//		EditText editText = (EditText) findViewById(R.id.edit_message);
//		String message = editText.getText().toString();
//		intent.putExtra(EXTRA_MESSAGE, message);
//		
//		startActivity(intent);
//		
//	}
	
	private final LocationListener listener = new LocationListener(){
		
	}

}
