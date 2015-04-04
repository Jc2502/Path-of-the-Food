package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.pathofthefood.flyingburger.Address.AddressBook;
import com.pathofthefood.flyingburger.User.User;
import com.pathofthefood.flyingburger.utils.HttpClientHelp;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity {
    String value, token, usr_id;
    EditText mUserEdit, mPassEdit;
    Button LogInButton;
    TextView registroTextView;
    boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mUserEdit = (EditText) findViewById(R.id.userText);
        mPassEdit = (EditText) findViewById(R.id.passwordText);

        LogInButton = (Button) findViewById(R.id.loginButton);
        registroTextView = (TextView) findViewById(R.id.textViewRegistro);

        registroTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), User_Register.class));


            }
        });

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!login) {

                    if(!CONFIG.isOnline(getApplicationContext())){
                        Toast.makeText(getApplicationContext(), R.string.check_internet,Toast.LENGTH_LONG).show();
                        return;
                    }

                    mUserEdit.setError(null);

                    mPassEdit.setError(null);


                    String usr = mUserEdit.getText().toString();
                    String pass = mPassEdit.getText().toString();
                    boolean cancel = false;
                    View focusView = null;
                    if (TextUtils.isEmpty(usr)) {
                        mUserEdit.setError(getString(R.string.error_field_required));
                        focusView = mUserEdit;
                        cancel = true;
                    }

                    if(TextUtils.isEmpty(pass) && !cancel){
                        mPassEdit.setError(getString(R.string.error_field_required));
                        focusView = mPassEdit;
                        cancel  = true;
                    }

                    if (cancel) {
                        LogInButton.setEnabled(true);
                        login = false;
                        focusView.requestFocus();

                    } else {
                        try {
                            login(usr, pass);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

        });
    }

    public void login(String usr, String pass) throws JSONException {
        new LoginTask(getApplicationContext(), usr, pass, LogInButton).execute();
    }


    class LoginTask extends AsyncTask<String, Void, Boolean> {


        Bundle id;
        private Context context;
        private String acUser;
        private String acPass;
        private String message;
        private Button loginButton;
        private SessionManager session;

        public LoginTask(Context ctx, String acUser, String acPass, Button loginButton) {
            this.context = ctx;
            this.acUser = acUser;
            this.acPass = acPass;
            this.loginButton = loginButton;
            this.loginButton.setEnabled(false);
            this.session = new SessionManager(getApplicationContext());

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("LoginTask", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LoginTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.Login(CONFIG.SERVER_URL, this.acUser, this.acPass);
                Gson gson = new Gson();
                value = jsonObject.toString();
                Log.e("JSON  ", value);
                if (value != null && !jsonObject.getBoolean("error")) {

                        session.createLoginSession(gson.fromJson(jsonObject.getString("user"), User.class));


                    token = jsonObject.getJSONObject("user").getString("api_token");
                    usr_id = jsonObject.getJSONObject("user").getString("id");
                    Log.e("Api_Token-->", token);
                    Log.e("User_ID-->", usr_id);
                    id = new Bundle();
                    id.putSerializable("token", token);
                    id.putSerializable("usr_id", usr_id);
                    return false;
                } else {
                    this.message = "No se ha podido Iniciar Session";
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
                login = true;
                Toast.makeText(this.context, "Loggeo Exitosamente!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AddressBook.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }


}
