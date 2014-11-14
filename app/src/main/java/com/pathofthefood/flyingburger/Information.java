package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Information extends Activity {
    User users;
    String api, id;
    Button edit, editpass;
    EditText mUserEdit, mEmailEdit, mPhoneEdit, mFullnameEdit, pass1, pass2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        mUserEdit = (EditText) findViewById(R.id.editUser);
        mEmailEdit = (EditText) findViewById(R.id.editEmail);
        mPhoneEdit = (EditText) findViewById(R.id.editPhone);
        mFullnameEdit = (EditText) findViewById(R.id.editFullName);
        edit = (Button) findViewById(R.id.buttonedit);
        editpass = (Button) findViewById(R.id.buttoneditpass);
        pass1 = (EditText) findViewById(R.id.editpass1);
        pass2 = (EditText) findViewById(R.id.editpass2);

        users = (User) getIntent().getSerializableExtra("users");
        if (users != null) {
            api = users.getApi_token();
            id = users.getId();
            mUserEdit.setText(users.getUsername());
            mEmailEdit.setText(users.getEmail());
            mPhoneEdit.setText(users.getPhone());
            mFullnameEdit.setText(users.getFullname());
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userS = mUserEdit.getText().toString();
                String emailS = mEmailEdit.getText().toString();
                String phoneS = mPhoneEdit.getText().toString();
                String fullnameS = mFullnameEdit.getText().toString();

                edit.setEnabled(false);
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(userS)) {
                    mUserEdit.setError(getString(R.string.error_field_required));
                    focusView = mUserEdit;
                    cancel = true;
                }
                if (TextUtils.isEmpty(emailS)) {
                    mEmailEdit.setError(getString(R.string.error_field_required));
                    focusView = mEmailEdit;
                    cancel = true;
                }
                if (TextUtils.isEmpty(phoneS)) {
                    mPhoneEdit.setError(getString(R.string.error_field_required));
                    focusView = mPhoneEdit;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fullnameS)) {
                    mFullnameEdit.setError(getString(R.string.error_field_required));
                    focusView = mFullnameEdit;
                    cancel = true;
                }

                if (cancel) {
                    edit.setEnabled(true);
                    focusView.requestFocus();
                }else{
                    edit_user(api, id, mUserEdit.getText().toString(), mEmailEdit.getText().toString(), mPhoneEdit.getText().toString(), mFullnameEdit.getText().toString());
                }
            }
        });

        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass1.getText().toString().equals(pass2.getText().toString())) {
                    try {
                        edit_pass(api, id, String.valueOf(pass1.getText()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pass2.setError("Las contraseñas no coinciden");
                }
            }
        });

    }

    public void edit_user(String token, String id, String user, String email, String phone, String fullname) {
        new EditUserTask(getApplicationContext(), token, id, user, email, phone, fullname, edit).execute();
    }

    public void edit_pass(String token, String id, String password) throws JSONException {
        new EditPassTask(getApplicationContext(), token, id, password, editpass).execute();
    }

    class EditUserTask extends AsyncTask<String, Void, Boolean> {
        String value;
        private Context context;
        private String api, id, user, email, phone, fullname;
        private Button editButton;
        private String message;
        private HashMap<String, String> errors;

        public EditUserTask(Context ctx, String api, String id, String user, String email, String phone, String fullname, Button editButton) {
            this.context = ctx;
            this.api = api;
            this.id = id;
            this.user = user;
            this.email = email;
            this.phone = phone;
            this.fullname = fullname;
            this.editButton = editButton;
            this.editButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("Logout", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LogoutTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.edit_user(CONFIG.SERVER_URL, this.api, this.id, this.user, this.email, this.phone, this.fullname);
                value = jsonObject.toString();
                Log.d("EditUserTask", value);
                if (value == null) {
                    this.message = "Error Inesperado";
                    Log.d("UserEdit", "ErrorLogout");
                    return true;
                }



                if(jsonObject.getBoolean("error") && jsonObject.has("messages")){
                    Log.e("EditUserTask", "Error true, messages");
                   this.errors = new HashMap<String, String>();
                   JSONObject messages = jsonObject.getJSONObject("messages");
                    if(messages.has("username")){
                        errors.put("username", messages.getJSONArray("username").getString(0));
                    }
                    if(messages.has("phone")){
                        errors.put("phone", messages.getJSONArray("phone").getString(0));
                    }

                    if(messages.has("email")){
                        errors.put("email", messages.getJSONArray("email").getString(0));
                    }
                    return true;
                }else if(jsonObject.getBoolean("error") && jsonObject.has("message")){
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
            if (!error && this.errors == null) {
                Toast.makeText(this.context, "Usuario Editado!", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();

            } else if(error && this.errors != null) {
                Toast.makeText(this.context, "Hay ERRORS", Toast.LENGTH_SHORT).show();

                View focusView = null;
                if(this.errors.containsKey("username")){
                    mUserEdit.setError(this.errors.get("username"));
                    focusView = mUserEdit;
                }
                if(this.errors.containsKey("email")){
                    mEmailEdit.setError(this.errors.get("email"));
                    focusView = mEmailEdit;
                }
                if(this.errors.containsKey("phone")){
                    mPhoneEdit.setError(this.errors.get("phone"));
                    focusView = mPhoneEdit;
                }
                focusView.requestFocus();
            }else{
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }

    class EditPassTask extends AsyncTask<String, Void, Boolean> {
        String value;
        private Context context;
        private String api, id, password;
        private Button loginButton;
        private String message;

        public EditPassTask(Context ctx, String api, String id, String password, Button loginButton) {
            this.context = ctx;
            this.api = api;
            this.id = id;
            this.password = password;
            this.loginButton = loginButton;
            this.loginButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("Logout", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LogoutTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.edit_pass(CONFIG.SERVER_URL, this.api, this.id, this.password);
                value = jsonObject.toString();
                Log.e("JSON  ", value);
                if (value != null) {
                    return false;
                } else {
                    this.message = "Error Logout";
                    Log.d("LogoutTask", "ErrorLogout");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                this.message = "ERROR";
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.message = "Error Inesperado";
                return true;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("PasswordEditTask", "Entra a onPostExecute..");
            this.loginButton.setEnabled(true);
            if (!result) {

                Toast.makeText(this.context, "Password Editado!", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }

}
