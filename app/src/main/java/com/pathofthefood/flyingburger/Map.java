package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.GPSTracker;


public class Map extends Activity {

    /**
     * Local variables *
     */
    GPSTracker gps;
    GoogleMap googleMap;
    double lat1,lat2,lon1,lon2;
    private DrawerArrowDrawable drawerArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        gps = new GPSTracker(Map.this);
        Intent intent = getIntent();
        lat1 = Double.valueOf(intent.getDoubleExtra("lat1", 0));
        lon1 = Double.valueOf(intent.getDoubleExtra("lon1", 0));
        lat2 = Double.valueOf(intent.getDoubleExtra("lat2",0));
        lon2 = Double.valueOf(intent.getDoubleExtra("lon2",0));
        createMapView();
        addMarker();

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void createMapView() {
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     * Adds a marker to the map
     */
    private void addMarker() {
        if (null != googleMap) {
            LatLng currentPosition,restPosition;
            if(gps.canGetLocation()) {

                //double latitude = gps.getLatitude();
                //double longitude = gps.getLongitude();
                //currentPosition = new LatLng(latitude, longitude);

                currentPosition = new LatLng(lat1, lon1);
                restPosition = new LatLng(lat2,lon2);
                googleMap.addMarker(new MarkerOptions()
                                .position(currentPosition)
                                .title("Address")
                                .draggable(true)
                );
                googleMap.addMarker(new MarkerOptions()
                                .position(restPosition)
                                .title("rest")
                                .draggable(true)
                );
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15.0f));
            }
           else{
                gps.showSettingsAlert();
            }
        }
    }

}
