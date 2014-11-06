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
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class User_Register extends Activity {

    EditText user, pass, email, phone, fullname;
    Button registro;
    ImageView x1, x2, x3, x4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__register);

        user = (EditText) findViewById(R.id.editTextUSR);
        pass = (EditText) findViewById(R.id.editTextPWD);
        email = (EditText) findViewById(R.id.editTextEA);
        phone = (EditText) findViewById(R.id.editTextPN);
        fullname = (EditText) findViewById(R.id.editTextFN);
        registro = (Button) findViewById(R.id.buttonRegistro);
        x1 = (ImageView) findViewById(R.id.imageView4);
        x2 = (ImageView) findViewById(R.id.imageView5);
        x3 = (ImageView) findViewById(R.id.imageView6);
        x4 = (ImageView) findViewById(R.id.imageView7);
        x1.setVisibility(View.INVISIBLE);
        x2.setVisibility(View.INVISIBLE);
        x3.setVisibility(View.INVISIBLE);
        x4.setVisibility(View.INVISIBLE);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = user.getText().toString();
                String pwd = pass.getText().toString();
                String mail = email.getText().toString();
                String phne = phone.getText().toString();
                String fulln = fullname.getText().toString();
                //Log.d("VALORES: ", usr + "||" + pass);

                if (usr.equals("") || pwd.equals("") || mail.equals("") || phne.equals("") || fulln.equals("")) {
                    Toast.makeText(getApplicationContext(), "Alguno de los campos vacios", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        register(usr, pwd, mail, phne, fulln);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void register(String usr, String pwd, String mail, String phne, String fulln) throws JSONException {
        new RegisterTask(getApplicationContext(), usr, pwd, mail, phne, fulln, registro).execute();
    }


    class RegisterTask extends AsyncTask<String, Void, Boolean> {



        private Context context;
        private String usr;
        private String pwd;
        private String mail;
        private String phne;
        private String fulln;
        private String message;
        private Button registerButton;

        public RegisterTask(Context ctx, String usr, String pwd, String mail, String phne, String fulln, Button registerButton) {
            this.context = ctx;
            this.usr = usr;
            this.pwd = pwd;
            this.mail = mail;
            this.phne = phne;
            this.fulln = fulln;
            this.registerButton = registerButton;
            this.registerButton.setEnabled(false);
        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("LoginTask", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LoginTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.register(CONFIG.SERVER_URL, this.usr, this.pwd, this.mail, this.phne, this.fulln);

                String value = String.valueOf(jsonObject);
                Log.e("JSON  ", value);
                if (value != null) {
                    String error = String.valueOf(jsonObject.get("error"));
                    if (error == "true") {
                        JSONObject jsonmsj = jsonObject.getJSONObject("messages");
                        if (jsonmsj.length() > 1) {
                            String msj = "";
                            if (jsonmsj.has("username")) {
                                msj = msj + String.valueOf(jsonmsj.getJSONArray("username").get(0)) + "\n";

                            }

                            if (jsonmsj.has("email")) {
                                msj = msj + String.valueOf(jsonmsj.getJSONArray("email").get(0)) + "\n";

                            }
                            if (jsonmsj.has("phone")) {
                                msj = msj + String.valueOf(jsonmsj.getJSONArray("phone").get(0)) + "\n";
                            }
                            this.message = msj;
                        }
                        else{
                            if (jsonmsj.has("username")) {
                                this.message = String.valueOf(jsonmsj.getJSONArray("username").get(0));
                               }
                            if (jsonmsj.has("email")) {
                                this.message = String.valueOf(jsonmsj.getJSONArray("email").get(0));
                                }
                            if (jsonmsj.has("phone")) {
                                this.message = String.valueOf(jsonmsj.getJSONArray("phone").get(0));
                                }
                        }
                        return true;
                    }
                    return false;
                } else {
                    this.message = "Error Registro";
                    Log.d("LoginTask", this.message);
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                this.message = "Error Raro";
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.message = "Error Inesperado";
                return true;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("RegistroTask", "Entra a onPostExecute..");
            this.registerButton.setEnabled(true);
            if (!result) {

                Toast.makeText(this.context, "Se Registro Exitosamente!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
