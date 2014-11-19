package com.pathofthefood.flyingburger.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.*;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.HttpClientHelp;
import com.pathofthefood.flyingburger.utils.NotAuthException;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Menu extends Activity {

    private static ArrayList<Products> mProductList;
    private ProductAdapter mProductAdapter;
    private SessionManager session;
    ListView listViewCatalog;
    private List<Products> mCartList;
    FloatingActionButton viewShoppingCart;
    private DrawerArrowDrawable drawerArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        final String address = intent.getStringExtra("address");
        String restaurant = intent.getStringExtra("restaurant");
        final double lat1 = Double.valueOf(intent.getDoubleExtra("lat1", 0));
        final double lon1 = Double.valueOf(intent.getDoubleExtra("lon1", 0));
        final double lat2 = Double.valueOf(intent.getDoubleExtra("lat2",0));
        final double lon2 = Double.valueOf(intent.getDoubleExtra("lon2",0));
        Log.d("RESTAURANT", restaurant);
        listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);

        new MenuTask(getApplicationContext(), mProductList, session.getUserDetails().getApi_token(), restaurant).execute();

        viewShoppingCart = (FloatingActionButton) findViewById(R.id.ButtonViewCart);
        viewShoppingCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent viewShoppingCartIntent = new Intent(getBaseContext(), ShoppingCartActivity.class).putExtra("address",address).putExtra("lat1",lat1).putExtra("lat2",lat2).putExtra("lon1",lon1).putExtra("lon2",lon2);
                startActivity(viewShoppingCartIntent);
            }
        });

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener listener=new View.OnClickListener(){
        @Override
        public void onClick(View arg0) {
            //Refresh cache directory downloaded images
            mProductAdapter.imageLoader.clearCache();
            mProductAdapter.notifyDataSetChanged();
        }
    };

    public static ArrayList<Products> getCatalog(Resources res) {
        return mProductList;
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        ShoppingCartHelper.removeCart();
        super.onBackPressed();
    }

    class MenuTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private ArrayList<Products> address;
        private String api, id;

        public MenuTask(Context context, ArrayList<Products> address, String api, String id) {
            this.context = context;
            this.address = address;
            this.api = api;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                if (session.isLoggedIn() && CONFIG.isOnline(this.context)) {
                    HttpClientHelp clienteHttp = new HttpClientHelp();
                    this.address = clienteHttp.show_menu(CONFIG.SERVER_URL, api, id);
                    if (this.address == null || this.address.size() == 0) {
                        return CONFIG.ERROR_NULL;
                    }
                    return CONFIG.DONE;
                } else if (session.isLoggedIn() && !CONFIG.isOnline(this.context)) {
                    return CONFIG.DONE;
                }
                return CONFIG.ERROR_NOT_AUTH;
            } catch (JSONException e) {
                return CONFIG.ERROR_JSON;
            } catch (NotAuthException e) {
                return CONFIG.ERROR_NOT_AUTH;
            } catch (Exception ex) {
                return CONFIG.ERROR_JSON;

            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case CONFIG.DONE:
                    Log.d("AddressTask", "Entro onPostExecute");
                    mProductList = this.address;
                    mProductAdapter = new ProductAdapter(mProductList, context, false);
                    Log.e("ARRAYLIST", String.valueOf(mProductList.get(0)));
                    mProductAdapter.notifyDataSetChanged();
                    Log.e("ARRAYLIST", String.valueOf(mProductList.get(0)));
                    listViewCatalog.setAdapter(mProductAdapter);
                    viewShoppingCart.attachToListView(listViewCatalog);
                    Log.e("ARRAYLIST", String.valueOf(mProductList.get(0)));
                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
                case CONFIG.ERROR_NULL:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu.this);
                    alertDialog.setTitle("Alertas");
                    alertDialog.setMessage("No Menu");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    break;
                default:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
            }
        }
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
}
