package com.pathofthefood.flyingburger.Menu;

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
import com.pathofthefood.flyingburger.*;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends Activity {

    Button address;
    FloatingActionButton checkout;
    private ArrayList<Address> addresses;
    private static List<Products> mCartList;
    private ProductAdapter mProductAdapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);
        session = new SessionManager(getApplicationContext());
        checkout = (FloatingActionButton) findViewById(R.id.fab_shop);


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

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Valor = 0.0;
                String ids = "";
                for(int i=0; i< mCartList.size();i++){
                  Valor = mCartList.get(i).getPrice()+Valor;
                  ids = mCartList.get(i).getId()+","+ids;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(Valor)+"\n"+ids, Toast.LENGTH_LONG).show();
                //startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });


    }
    public static void remove(){
        mCartList = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the data
        if (mProductAdapter != null) {
            mProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        remove();
        ShoppingCartHelper.removeCart();
        super.onBackPressed();
    }

}
