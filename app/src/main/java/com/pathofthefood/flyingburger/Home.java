package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Home extends Activity {
    Button LogOut, EditInfo, Delete;

    private List<Product> mProductList;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mProductList = ShoppingCartHelper.getCatalog(getResources());
        LogOut = (Button) findViewById(R.id.buttonlogout);
        EditInfo = (Button) findViewById(R.id.buttonedit);
        Delete = (Button) findViewById(R.id.buttondelete);
        session = new SessionManager(getApplicationContext());

        ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        listViewCatalog.setAdapter(new ProductAdapter(mProductList, getLayoutInflater(), false));

        listViewCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ShoppingCartHelper.PRODUCT_INDEX, position);
                startActivity(productDetailsIntent);
            }
        });

        Button viewShoppingCart = (Button) findViewById(R.id.ButtonViewCart);
        viewShoppingCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent viewShoppingCartIntent = new Intent(getBaseContext(), ShoppingCartActivity.class);
                startActivity(viewShoppingCartIntent);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    logout(session.getUserDetails().getApi_token());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });

        EditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!CONFIG.isOnline(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), R.string.check_internet,Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    user_info();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    delete_user(session.getUserDetails().getApi_token(), session.getUserDetails().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void logout(String token) throws JSONException {
        new LogOutTask(getApplicationContext(), token, LogOut).execute();
    }

    public void user_info() throws JSONException {
        new UserInfoTask(getApplicationContext(), EditInfo).execute();
    }

    public void delete_user(String token, String api) throws JSONException {
        new DeleteUserTask(getApplicationContext(), token, api, Delete).execute();
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
            Log.d("Logout", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LogoutTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.logout(CONFIG.SERVER_URL, this.api);
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
            Log.d("LogoutTask", "Entra a onPostExecute..");
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

    class DeleteUserTask extends AsyncTask<String, Void, Boolean> {

        String value;

        private Context context;
        private String api, id;
        private Button loginButton;
        private String message;

        public DeleteUserTask(Context ctx, String api, String id, Button loginButton) {
            this.context = ctx;
            this.api = api;
            this.id = id;

            this.loginButton = loginButton;
            this.loginButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("Logout", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LogoutTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.delete_user(CONFIG.SERVER_URL, this.api, this.id);
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
            Log.d("LogoutTask", "Entra a onPostExecute..");
            this.loginButton.setEnabled(true);
            if (!result) {

                Toast.makeText(this.context, "Usuario Eliminado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }

    class UserInfoTask extends AsyncTask<String, Void, Integer> {


        private Context context;
        private Button editButton;
        private User  user;
        private String message;
        private Bundle informacion;
        private SessionManager session;

        public UserInfoTask(Context ctx, Button editButton) {
            this.context = ctx;
            this.editButton = editButton;
            this.editButton.setEnabled(false);
            session = new SessionManager(getApplicationContext());

        }


        @Override
        protected Integer doInBackground(String... params) {
            Log.d("UserInfoTask", "Entra a doInBack..");


                Log.d("UserInfoTask", "Entra a doInBack..TRY");
                HttpClientHelp client = new HttpClientHelp();

                if (session.isLoggedIn() && CONFIG.isOnline(getApplicationContext())) {
                    //Obtenemos info del usuario
                    HttpClientHelp httpClientHelp = new HttpClientHelp();
                    try {
                        this.user = httpClientHelp.user_info(CONFIG.SERVER_URL, session.getUserDetails().getApi_token());
                        if (this.user == null) {
                            this.message = "NO EXISTE";
                            Log.d("EditTask", "ErrorEdit");
                            //Nulll
                            return CONFIG.ERROR_NULL;
                        } else {
                            informacion = new Bundle();
                            informacion.putSerializable("users", this.user);

                            return CONFIG.DONE;
                        }
                    } catch (JSONException e) {
                        return CONFIG.ERROR_JSON;
                    } catch (NotAuthException e) {
                        return CONFIG.ERROR_NOT_AUTH;
                    }
                } else if (session.isLoggedIn() && !CONFIG.isOnline(getApplicationContext())) {
                    return CONFIG.DONE;
                }
                return CONFIG.ERROR_NOT_AUTH;
            }



        @Override
        protected void onPostExecute(Integer result) {
            Log.d("EditInfo", "Entra a onPostExecute..");
            this.editButton.setEnabled(true);
            switch (result) {
                case CONFIG.DONE:
                    Intent edituser = new Intent(this.context, Information.class);
                    edituser.putExtras(informacion);
                    startActivity(edituser);
                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                    break;
                default:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                    break;
            }
        }

    }


}
