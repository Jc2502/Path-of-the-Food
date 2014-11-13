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
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pathofthefood.flyingburger.R;


public class NewAddress extends Activity {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        createMapView();
        //addMarker();


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
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLocation(), 18.0f));
                googleMap.addMarker(new MarkerOptions()
                        .position(getLocation())
                        .draggable(true));

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

    private LatLng getLocation() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String proveedor = manager.getBestProvider(criteria, true);
        Location localizacion = manager.getLastKnownLocation(proveedor);
        try {
            return new LatLng(localizacion.getLatitude(),
                    localizacion.getLongitude());
        } catch (Exception e) {
            return new LatLng(25.700336, -100.350814);
        }
    }
}
