package com.pathofthefood.flyingburger.Pedidos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.pathofthefood.flyingburger.CONFIG;
import com.pathofthefood.flyingburger.Login;
import com.pathofthefood.flyingburger.R;
import com.pathofthefood.flyingburger.Restaurant.RestaurantAdapter;
import com.pathofthefood.flyingburger.ldrawer_library.ActionBarDrawerToggle;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.HttpClientHelp;
import com.pathofthefood.flyingburger.utils.NotAuthException;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;

import java.util.ArrayList;

public class OrdersBook extends Activity {
    View v1;
    TextView tvu, tvm;
    private ListView AddressList;
    private ArrayList<Orders> addressess;
    private OrdersAdapter adapter;
    private SessionManager session;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private DrawerLayout drawer;
    private ListView leftDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private int history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_book);
        session = new SessionManager(getApplicationContext());
        AddressList = (ListView) findViewById(R.id.ListViewOrders);
        Intent intent = getIntent();
        history = intent.getIntExtra("history",0);
        new OrderTask(getApplicationContext(), addressess, session.getUserDetails().getApi_token(),history).execute();

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
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy()
    {
        // Remove adapter refference from list
        AddressList.setAdapter(null);
        super.onDestroy();
    }
    class OrderTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private ArrayList<Orders> orders;
        private String api;
        private int history;

        public OrderTask(Context context, ArrayList<Orders> orders, String api,int history) {
            this.context = context;
            this.orders = orders;
            this.api = api;
            this.history = history;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                if (session.isLoggedIn() && CONFIG.isOnline(this.context)) {
                    HttpClientHelp clienteHttp = new HttpClientHelp();
                    this.orders = clienteHttp.show_orders(CONFIG.SERVER_URL, api,history);
                    if (this.orders == null || this.orders.size() == 0) {
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
                    addressess = this.orders;
                    adapter = new OrdersAdapter(getApplicationContext(), addressess);
                    Log.e("ARRAYLIST", String.valueOf(addressess.get(0)));
                    adapter.notifyDataSetChanged();
                    AddressList.setAdapter(adapter);

                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
                case CONFIG.ERROR_NULL:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersBook.this);
                    alertDialog.setTitle("Alertas");
                    alertDialog.setMessage("No Direcciones registradas");
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
}
