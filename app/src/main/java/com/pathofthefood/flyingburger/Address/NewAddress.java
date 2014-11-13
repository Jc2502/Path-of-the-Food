package com.pathofthefood.flyingburger.Address;

/**
 * Created by Juan Acosta on 11/12/2014.
 */
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pathofthefood.flyingburger.R;


public class NewAddress extends Activity {

    GoogleMap googleMap;
    LatLng currentPosition;
    LocationManager locationManager;
    Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        createMapView();
        addMarker();
    }


    private void createMapView() {
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapViewNA)).getMap();

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

        /** Make sure that the map has been initialised **/
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        if (null != googleMap) {
            currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .title("MI CASA")
                            .draggable(true)
            );
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18.0f));
        }
    }
}
