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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Home extends Activity {
    Button LogOut, EditInfo,Delete;
    String Token;
    private List<Product> mProductList;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mProductList = ShoppingCartHelper.getCatalog(getResources());
        LogOut = (Button) findViewById(R.id.buttonlogout);
        EditInfo = (Button) findViewById(R.id.buttonedit);
        Delete = (Button) findViewById(R.id.buttondelete);

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

        EditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Token = extras.getString("token");
                    Log.e("EXTRAS", Token);
                    // and get whatever type user account id is
                    try {
                        user_info(Token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("NO EXTRA", "NO EXTRA");
                }
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String id = extras.getString("usr_id");
                    Token = extras.getString("token");
                    Log.e("EXTRAS", Token+"-----"+id);
                    // and get whatever type user account id is
                    try {
                        delete_user(Token,id);
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

    public void user_info(String token) throws JSONException {
        new UserInfoTask(getApplicationContext(), token, EditInfo,users).execute();
    }

    public void delete_user(String token, String api)throws JSONException{new DeleteUserTask(getApplicationContext(),token,api,Delete).execute();}

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
        private String api,id;
        private Button loginButton;
        private String message;

        public DeleteUserTask(Context ctx, String api,String id, Button loginButton) {
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

    class UserInfoTask extends AsyncTask<String, Void, Boolean> {



        private Context context;
        private String api;
        private Button editButton;
        private ArrayList<User> users;
        private String message;
        private Bundle informacion;

        public UserInfoTask(Context ctx, String api, Button editButton,ArrayList<User> users) {
            this.context = ctx;
            this.api = api;
            this.users = users;
            this.editButton = editButton;
            this.editButton.setEnabled(false);

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("UserInfoTask", "Entra a doInBack..");

            try {
                Log.d("UserInfoTask", "Entra a doInBack..TRY");
                HttpClientHelp client = new HttpClientHelp();
                this.users = client.user_info(CONFIG.SERVER_URL, this.api);

                if (this.users == null) {

                    this.message = "NO EXISTE";
                    Log.d("EditTask", "ErrorEdit");
                    return true;
                } else {
                    informacion = new Bundle();
                    informacion.putSerializable("users", this.users);

                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                this.message = "Error";
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.message = "Error Inesperado";
                return true;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("EditInfo", "Entra a onPostExecute..");
            this.editButton.setEnabled(true);
            if (!result) {

                Toast.makeText(this.context, "Edit User Info", Toast.LENGTH_SHORT).show();
                Intent edituser = new Intent (this.context, Information.class);
                edituser.putExtras(informacion);
                startActivity(edituser);

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }


}
