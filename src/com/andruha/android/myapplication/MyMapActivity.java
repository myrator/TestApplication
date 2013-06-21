package com.andruha.android.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.andruha.android.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapActivity extends Activity {
	  TextView tvLoc;
	  LatLng curloc;
	  private GoogleMap map;
	  LocationManager locationManager;
	  LocationListener locationListener;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
		        
		    @Override
		    public void onStatusChanged(String provider, int status, Bundle extras) {}
		            
		    @Override
		    public void onProviderEnabled(String provider) {}
		            
		    @Override
		    public void onProviderDisabled(String provider) {}
		      
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				updateLocation();
			}
		};
	
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		updateLocation();
}
	
	private void updateLocation(){
		Location location; 
		  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		  if (location != null) {
			curloc = new LatLng( location.getLatitude(), location.getLongitude());
			tvLoc = (TextView) findViewById(R.id.tvLocation);
			tvLoc.setText(getResources().getString(R.string.map_loc) + "\n "  + getResources().getString(R.string.latitude) +  curloc.latitude + "; \n" + getResources().getString(R.string.longitude) + curloc.longitude);
			
			
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        Marker curlocmark = map.addMarker(new MarkerOptions().position(curloc).title("Location"));
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curloc, 15));
	        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		  }
		  else {
			  tvLoc.setText("Cannot find current location");
		  }
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, getResources().getString(R.string.pref));
        mi.setIntent(new Intent(this, MyPreferenceActivity.class));
        return super.onCreateOptionsMenu(menu);
      }

}
