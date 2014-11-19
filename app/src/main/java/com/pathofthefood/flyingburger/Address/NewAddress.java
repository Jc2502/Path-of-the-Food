package com.pathofthefood.flyingburger.Address;

/**
 * Created by Juan Acosta on 11/12/2014.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.CONFIG;
import com.pathofthefood.flyingburger.utils.GPSTracker;
import com.pathofthefood.flyingburger.utils.HttpClientHelp;
import com.pathofthefood.flyingburger.R;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class NewAddress extends Activity {

    GoogleMap googleMap;
    FloatingActionButton save;
    EditText mLabel, mDescription, mTextAddress;
    GPSTracker gps;
    double latitude, longitude;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        session = new SessionManager(getApplicationContext());
        mLabel = (EditText) findViewById(R.id.editAddressName);
        mDescription = (EditText) findViewById(R.id.editdescription);
        mTextAddress = (EditText) findViewById(R.id.editTextAddress);
        save = (FloatingActionButton) findViewById(R.id.buttonSaveAddress);
        createMapView();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = mLabel.getText().toString();
                String description = mDescription.getText().toString();
                String textAddres = mTextAddress.getText().toString();
                save.setEnabled(false);
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(label)) {
                    mLabel.setError(getString(R.string.error_field_required));
                    focusView = mLabel;
                    cancel = true;
                }
                if (TextUtils.isEmpty(description)) {
                    mDescription.setError(getString(R.string.error_field_required));
                    focusView = mDescription;
                    cancel = true;
                }
                if (TextUtils.isEmpty(textAddres)) {
                    mTextAddress.setError(getString(R.string.error_field_required));
                    focusView = mTextAddress;
                    cancel = true;
                }
                if (cancel) {
                    save.setEnabled(true);
                    focusView.requestFocus();
                } else {
                    edit_user(session.getUserDetails().getApi_token(), mLabel.getText().toString(), mDescription.getText().toString(), mTextAddress.getText().toString(), String.valueOf(latitude), String.valueOf(longitude));
                }

            }
        });


    }

    public void edit_user(String token, String label, String details, String textaddress, String lat, String lon) {
        new AddAddressTask(getApplicationContext(), token, label, details, textaddress, lat, lon, save).execute();
    }

    private void createMapView() {
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            gps = new GPSTracker(NewAddress.this);
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapViewNA)).getMap();
                LatLng currentPosition;
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    currentPosition = new LatLng(latitude, longitude);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(false);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18.0f));
                    googleMap.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .draggable(true));

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18.0f));
                } else {
                    gps.showSettingsAlert();
                }

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    class AddAddressTask extends AsyncTask<String, Void, Boolean> {
        String value;
        private Context context;
        private String api, label, description, address, lat, lon;
        private FloatingActionButton editButton;
        private String message;
        private HashMap<String, String> errors;

        public AddAddressTask(Context ctx, String api, String label, String description, String address, String lat, String lon, FloatingActionButton editButton) {
            this.context = ctx;
            this.api = api;
            this.label = label;
            this.description = description;
            this.address = address;
            this.lat = lat;
            this.lon = lon;
            this.editButton = editButton;
            this.editButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("Logout", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LogoutTask", "Entra a doInBack..TRY");
                Log.e("LONGTUDE", this.lon);
                jsonObject = HttpClientHelp.add_address(CONFIG.SERVER_URL, this.api, this.label, this.description, this.address, this.lat, this.lon);
                value = jsonObject.toString();
                Log.d("EditUserTask", value);
                if (value == null) {
                    this.message = "Error Inesperado";
                    Log.d("UserEdit", "ErrorLogout");
                    return true;
                }


                if (jsonObject.getBoolean("error") && jsonObject.has("messages")) {
                    Log.e("EditUserTask", "Error true, messages");
                    this.errors = new HashMap<String, String>();
                    JSONObject messages = jsonObject.getJSONObject("messages");
                    if (messages.has("label")) {
                        errors.put("label", messages.getJSONArray("label").getString(0));
                    }
                    if (messages.has("description")) {
                        errors.put("description", messages.getJSONArray("description").getString(0));
                    }

                    if (messages.has("textaddress")) {
                        errors.put("textaddress", messages.getJSONArray("textaddress").getString(0));
                    }

                    if (messages.has("longitude")) {
                        errors.put("longitude", messages.getJSONArray("longitude").getString(0));
                    }
                    if (messages.has("latitude")) {
                        errors.put("latitude", messages.getJSONArray("latitude").getString(0));
                    }


                    return true;
                } else if (jsonObject.getBoolean("error") && jsonObject.has("message")) {
                    Log.e("EditUserTask", "Error true, messages");
                    this.message = jsonObject.getString("message");
                    return true;
                }
                Log.e("EditUserTask", "Erro FALSE");
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                this.message = "Error Intesperado, intentalo de nuevo";
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.message = "Error Inesperado";
                return true;
            }
        }


        @Override
        protected void onPostExecute(Boolean error) {
            Log.d("LogoutTask", "Entra a onPostExecute..");
            this.editButton.setEnabled(true);
            //Si no hay error
            if ((!error && this.errors == null) || (!error && this.errors.size() == 0)) {
                Toast.makeText(this.context, "Usuario Editado!", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();

            } else if (error && this.errors != null) {
                Toast.makeText(this.context, "Hay ERRORES", Toast.LENGTH_SHORT).show();

                View focusView = null;
                if (this.errors.containsKey("label")) {
                    mLabel.setError(this.errors.get("label"));
                    focusView = mDescription;
                }
                if (this.errors.containsKey("description")) {
                    mDescription.setError(this.errors.get("description"));
                    focusView = mDescription;
                }
                if (this.errors.containsKey("textaddress")) {
                    mTextAddress.setError(this.errors.get("textaddress"));
                    focusView = mTextAddress;
                }
                Log.e("ERRORES", String.valueOf(this.errors));
                focusView.requestFocus();
            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
