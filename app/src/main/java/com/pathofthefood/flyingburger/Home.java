package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class Home extends Activity {
    Button LogOut;
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LogOut = (Button) findViewById(R.id.button);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Token = extras.getString("token");
                    Log.e("EXTRAS", Token);
                    // and get whatever type user account id is
                    try {
                        logout(Token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("NO EXTRA", "NO EXTRA");
                }

            }


        });
    }

    public void logout(String token) throws JSONException {
        new LogOutTask(getApplicationContext(), token, LogOut).execute();
    }

    class LogOutTask extends AsyncTask<String, Void, Boolean> {

        String value;

        private Context context;
        private String api;
        private Button loginButton;
        private String message;

        public LogOutTask(Context ctx, String api, Button loginButton) {
            this.context = ctx;
            this.api = api;

            this.loginButton = loginButton;
            this.loginButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("LoginTask", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LoginTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.logout(CONFIG.SERVER_URL, this.api);

                //SessionManager session = new SessionManager(this.context);

                //value = jsonObject.getString(KEY_ID);
                value = jsonObject.toString();
                Log.e("JSON  ", value);
                if (value != null) {
                    //token = jsonObject.getJSONObject("user").getString("api_token");
                    //Log.e("Api_Token-->", token);
                    //access = jsonObject.getString(KEY_ACCES);
                    //moderator = jsonObject.getString(KEY_MODERATOR);
                    //Log.d("JSON ", id);
                    //session.createLogin(id, access, moderator);
                    return false;
                } else {
                    this.message = "Error Login";
                    Log.d("LoginTask", "ErrorLogin");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                this.message = "Usuario o Contraseña invalidos";
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.message = "Error Inesperado";
                return true;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("LoginTask", "Entra a onPostExecute..");
            this.loginButton.setEnabled(true);
            if (!result) {

                Toast.makeText(this.context, "Cerro Sesion!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
