package com.pathofthefood.flyingburger.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.Address.Address;
import com.pathofthefood.flyingburger.*;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.HttpClientHelp;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends Activity {

    FloatingActionButton checkout;
    private ArrayList<Address> addresses;
    private static List<Products> mCartList;
    private ProductAdapter mProductAdapter;
    private SessionManager session;
    private TextView total;
    String address;
    private DrawerArrowDrawable drawerArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);
        session = new SessionManager(getApplicationContext());
        checkout = (FloatingActionButton) findViewById(R.id.fab_shop);
        Intent intent = getIntent();
       final String address = intent.getStringExtra("address");
        final double lat1 = Double.valueOf(intent.getDoubleExtra("lat1", 0));
        final double lon1 = Double.valueOf(intent.getDoubleExtra("lon1", 0));
        final double lat2 = Double.valueOf(intent.getDoubleExtra("lat2",0));
        final double lon2 = Double.valueOf(intent.getDoubleExtra("lon2",0));
        Log.e("ADDRESS",address);
        mCartList = ShoppingCartHelper.getCartList();

        // Make sure to clear the selections
        for (int i = 0; i < mCartList.size(); i++) {
            mCartList.get(i).selected = false;
        }

        // Create the list
        final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        mProductAdapter = new ProductAdapter(mCartList, getApplicationContext(), true);
        listViewCatalog.setAdapter(mProductAdapter);

        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                        mCartList.remove(position);
                mProductAdapter.notifyDataSetChanged();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Valor = 0.0,tot = 0.0;
                String ids = "";
                String quantity = "";
                final ArrayList<String> item_id = new ArrayList<String>();
                final ArrayList<String> item_quantity = new ArrayList<String>();
                for(int i=0; i< mCartList.size();i++){
                  Valor = mCartList.get(i).getPrice()+Valor;
                  ids = mCartList.get(i).getId()+","+ids;
                  quantity = String.valueOf(ShoppingCartHelper.getProductQuantity(mCartList.get(i)));
                  item_id.add(mCartList.get(i).getId());
                  item_quantity.add(quantity);
                  tot = (Valor*ShoppingCartHelper.getProductQuantity(mCartList.get(i)))+tot;

                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShoppingCartActivity.this);
                alertDialog.setTitle("Desea Terminar su Orden");
                alertDialog.setMessage("Total: $"+String.valueOf(tot));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new OrderTask(getApplicationContext(),item_id,item_quantity,address,mCartList.get(0).getRestaurant_id(),checkout,lat1,lat2,lon1,lon2).execute();
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                //Toast.makeText(getApplicationContext(), String.valueOf(Valor)+"\n"+ids+"\n"+quantity, Toast.LENGTH_LONG).show();

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

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the data
        if (mProductAdapter != null) {
            mProductAdapter.notifyDataSetChanged();
        }
    }

    class OrderTask extends AsyncTask<String, Void, Boolean> {

        Bundle id;
        private Context context;
        private ArrayList<String> id_item;
        private ArrayList<String>quantity_item;
        private String restaurant_id;
        private String address_id;
        private FloatingActionButton OrderButton;
        private String message;
        private SessionManager session;
        double lat1,lat2,lon1,lon2;

        public OrderTask(Context ctx,ArrayList<String> id_item, ArrayList<String>quantity_item,String address_id,String restaurant_id, FloatingActionButton OrderButton,double lat1,double lat2,double lon1,double lon2) {
            this.context = ctx;
            this.id_item = id_item;
            this.quantity_item = quantity_item;
            this.address_id = address_id;
            this.restaurant_id = restaurant_id;
            this.OrderButton = OrderButton;
            this.OrderButton.setEnabled(false);
            this.lat1 = lat1;
            this.lat2 = lat2;
            this.lon1 = lon1;
            this.lon2 = lon2;
            this.session = new SessionManager(getApplicationContext());

        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("LoginTask", "Entra a doInBack..");
            JSONObject jsonObject;
            try {
                Log.d("LoginTask", "Entra a doInBack..TRY");
                jsonObject = HttpClientHelp.set_orders(CONFIG.SERVER_URL, session.getUserDetails().getApi_token(), id_item, quantity_item, address_id, restaurant_id);
                Gson gson = new Gson();
                String value = jsonObject.toString();
                Log.e("JSON  ", value);
                if (value != null && !jsonObject.getBoolean("error")) {

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
            this.OrderButton.setEnabled(true);
            if (!result) {
                Toast.makeText(this.context, "Pedido Exitosamente!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Map.class).putExtra("lat1",lat1).putExtra("lat2",lat2).putExtra("lon1",lon1).putExtra("lon2",lon2));
                finish();

            } else {
                Toast.makeText(this.context, this.message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
