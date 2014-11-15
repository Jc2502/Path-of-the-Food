package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.Address.Address;
import com.pathofthefood.flyingburger.Address.AddressBook;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends Activity {

    Button  address;
    FloatingActionButton checkout;
    private ArrayList<Address> addresses;
    private List<Product> mCartList;
    private ProductAdapter mProductAdapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);
        session = new SessionManager(getApplicationContext());
        checkout = (FloatingActionButton) findViewById(R.id.fab_shop);
        address = (Button) findViewById(R.id.buttonAddress);


        mCartList = ShoppingCartHelper.getCartList();

        // Make sure to clear the selections
        for (int i = 0; i < mCartList.size(); i++) {
            mCartList.get(i).selected = false;
        }

        // Create the list
        final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        mProductAdapter = new ProductAdapter(mCartList, getLayoutInflater(), true);
        listViewCatalog.setAdapter(mProductAdapter);

        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ShoppingCartHelper.PRODUCT_INDEX, position);
                startActivity(productDetailsIntent);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "PEDIDO LISTO", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //direcciones(session.getUserDetails().getApi_token());
                startActivity(new Intent(getApplicationContext(), AddressBook.class));

            }
        });

    }

    public void direcciones(String api) {
        new AddressTask(getApplicationContext(), this.addresses, api).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the data
        if (mProductAdapter != null) {
            mProductAdapter.notifyDataSetChanged();
        }
    }

    class AddressTask extends AsyncTask<String, Void, Boolean> {

        private Context context;
        private ArrayList<Address> address;
        private String message;
        private String api;
        private Bundle addressbook;

        public AddressTask(Context context, ArrayList<Address> address, String api) {
            this.context = context;
            this.address = address;
            this.api = api;

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpClientHelp clienteHttp = new HttpClientHelp();
                this.address = clienteHttp.show_addressbook(CONFIG.SERVER_URL, api);
                if (this.address.size() != 0) {
                    Log.d("Address--->>>", String.valueOf(this.address.get(0)));
                    if (this.address == null) {
                        this.message = "No existen Direcciones en tu libreta de direcciones";
                    } else {
                        addressbook = new Bundle();
                        addressbook.putSerializable("addressbook", this.address);
                        SharedPerferencesObjects<Address> shaEx = new SharedPerferencesObjects<Address>(this.context);
                        for (Address addresses : address) {
                            //shaEx.saveData(Address.class.getName() + "_" + addresses.getId(), addresses);
                            Log.d("Address", Address.class.getName() + "_" + addresses.getId());
                        }

                    }
                } else {
                    Log.e("Addressbook", "No Tiene Nada");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Addressbook", "Error cargando direciones");
            } catch (Exception e) {
                Log.e("Addressbook", "Error inesperado");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("AddressTask", "Entro onPostExecute");
            startActivity(new Intent(this.context, AddressBook.class).putExtras(addressbook));
        }
    }
}
