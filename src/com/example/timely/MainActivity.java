package com.example.timely;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;


// deprecate later
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public final static String EXTRA_MESSAGE = "com.example.timely.MESSAGE";
	
	private TextView mLatLng;
	private LocationManager mLocationManager;
	private Handler mHandler;
	
	private boolean mUseFine;
	private boolean mUseBoth;
	
	// Keys to maintain UI state after rotation.
	private static final String KEY_FINE = "use_fine";
	private static final String KEY_BOTH = "use_both";
	
    // UI handler codes.
    private static final int UPDATE_ADDRESS = 1;
    private static final int UPDATE_LATLNG = 2;

    private static final int TEN_SECONDS = 10000;
    private static final int TEN_METERS = 10;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    
    // debugging purposes
    private static final String TAG = "MyApp";  

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// We should restore app state (if exists) after rotation
		if (savedInstanceState != null){
			mUseFine = savedInstanceState.getBoolean(KEY_FINE);
			mUseBoth = savedInstanceState.getBoolean(KEY_BOTH);
		} else {
			mUseFine = true;
			mUseBoth = false;
		}
		
		
		mLatLng = (TextView) findViewById(R.id.latlng);
		
		// Handler for updating the UI text fields
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
					case UPDATE_LATLNG:
						mLatLng.setText((String) msg.obj);
						break;
				}
			}
		};
		
//		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Starts background location tracking
		checkLocationService();
		
	}
	
    // Restores UI states after rotation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_FINE, mUseFine);
        outState.putBoolean(KEY_BOTH, mUseBoth);
    }

	
	protected void onResume(){
		super.onResume();
		setup();
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
	// Stop receiving location updates whenever Activity is inactive
	protected void onStop(){
		super.onStop();
		
		// remove later for constant location tracking
		mLocationManager.removeUpdates(listener);
	}

	// Set up fine/coarse location providers 
	private void setup(){
		Location gpsLocation = null;
		Location networkLocation = null;
		mLocationManager.removeUpdates(listener);
		
        mLatLng.setText(R.string.unknown);
		
		
//		if (mUseFine) { 
			gpsLocation = requestUpdatesFromProvider(
					LocationManager.GPS_PROVIDER, R.string.not_support_gps);
			
			// Update the UI immediately if location is obtained.
			if (gpsLocation != null){
				updateUILocation(gpsLocation);
			}
//		}
	}
	
	/**
	 * Check location service
	 */
	private void checkLocationService(){
		Log.d("CHECK_SERVICE", "Service running: " + (settings.getBoolean("locationService", false)?"YES":"NO"));
		
		if (settings.getBoolean("locationService", false)){
			return;
		}
		
		Intent mServiceIntent = new Intent(this, LocationService.class);
		startService(mServiceIntent);
	}
	
	/**
	 * Method to register location updates with desired location provider.
	 * 
	 * @param provider Name of requested provider.
	 * @param errorResId Resource id for the string message to be displayed if provider does not exist on device.
	 * @return A previously returned {@link android.location.Location} from the requested provider, if exists.
	 */
	private Location requestUpdatesFromProvider(final String provider, final int errorResId){
		Location location = null;
		if (mLocationManager.isProviderEnabled(provider)){
			mLocationManager.requestLocationUpdates(provider, TEN_SECONDS, TEN_METERS, listener);
			location = mLocationManager.getLastKnownLocation(provider);
		} else {
			Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
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
	
	private void updateUILocation(Location location){
		// We're sending the update ot a handler updates UI with new location
		Message.obtain(mHandler,
				UPDATE_LATLNG,
				location.getLatitude() + ", " + location.getLongitude()).sendToTarget();
	}
	
	private final LocationListener listener = new LocationListener(){
		
		@Override
		public void onLocationChanged(Location location){
			updateUILocation(location);
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
	};
}
