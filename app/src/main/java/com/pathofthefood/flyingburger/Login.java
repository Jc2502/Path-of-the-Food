package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity {
    String value, token;
    EditText User, Pass;
    Button LogIn;
    boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        User = (EditText) findViewById(R.id.userText);
        Pass = (EditText) findViewById(R.id.passwordText);

        LogIn = (Button) findViewById(R.id.loginButton);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!login) {
                    login = true;
                    String usr = User.getText().toString();
                    String pass = Pass.getText().toString();
                    Log.d("VALORES: ", usr + "||" + pass);

                    if (usr.equals("") || pass.equals("")) {
                        Toast.makeText(getApplicationContext(), "Alguno de los campos vacios", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        login(usr, pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });
    }

    public void login(String usr, String pass) throws JSONException {
        new LoginTask(getApplicationContext(), usr, pass, LogIn).execute();
    }


    class LoginTask extends AsyncTask<String, Void, Boolean> {



        private Context context;
        private String acUser;
        private String acPass;
        private String message;
        private Button loginButton;
        Bundle id;

        public LoginTask(Context ctx, String acUser, String acPass, Button loginButton) {
            this.context = ctx;
            this.acUser = acUser;
            this.acPass = acPass;
            this.loginButton = loginButton;
            this.loginButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("LoginTask", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LoginTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.Login(CONFIG.SERVER_URL, this.acUser, this.acPass);

                //SessionManager session = new SessionManager(this.context);

                //value = jsonObject.getString(KEY_ID);
                value = jsonObject.toString();
                Log.e("JSON  ", value);
                if (value != null) {
                    token = jsonObject.getJSONObject("user").getString("api_token");
                    Log.e("Api_Token-->", token);
                    id = new Bundle();
                    id.putSerializable("token", token);
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

                Toast.makeText(this.context, "Loggeo Exitosamente!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class).putExtras(id));
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }



}
