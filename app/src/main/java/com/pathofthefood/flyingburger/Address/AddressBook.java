package com.pathofthefood.flyingburger.Address;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.CONFIG;
import com.pathofthefood.flyingburger.HttpClientHelp;
import com.pathofthefood.flyingburger.Login;
import com.pathofthefood.flyingburger.NotAuthException;
import com.pathofthefood.flyingburger.R;
import com.pathofthefood.flyingburger.ldrawer_library.ActionBarDrawerToggle;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.SessionManager;

import org.json.JSONException;

import java.util.ArrayList;

public class AddressBook extends Activity implements AdapterView.OnItemClickListener {

    FloatingActionButton address_add;
    private ListView AddressList;
    private ArrayList<Address> addressess;
    private AddressAdapter adapter;
    private SessionManager session;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private DrawerLayout drawer;
    private ListView leftDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private String[] leftSliderData = {"Home", "Android", "Tech Zone", "Sitemap", "About", "Contact Me"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressbook);
        init();
        initDrawer();
        session = new SessionManager(getApplicationContext());

        AddressList = (ListView) findViewById(R.id.ListViewAddress);
        address_add = (FloatingActionButton) findViewById(R.id.fab);


        address_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), NewAddress.class));
            }
        });
        new AddressTask(getApplicationContext(), addressess, session.getUserDetails().getApi_token()).execute();
        //addressess = (ArrayList<Address>) getIntent().getSerializableExtra("addressbook");
    }

    private void init() {
        getActionBar().setIcon(new ColorDrawable(Color.TRANSPARENT));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        navigationDrawerAdapter=new ArrayAdapter<String>( AddressBook.this,android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        drawer.closeDrawers();
        Toast.makeText(AddressBook.this, leftSliderData[position].toString() + "", Toast.LENGTH_SHORT).show();
    }



    private void initDrawer() {
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,drawerArrow, R.string.drawer_open,R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    class AddressTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private ArrayList<Address> address;
        private String api;

        public AddressTask(Context context, ArrayList<Address> address, String api) {
            this.context = context;
            this.address = address;
            this.api = api;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                if (session.isLoggedIn() && CONFIG.isOnline(this.context)) {
                    HttpClientHelp clienteHttp = new HttpClientHelp();
                    this.address = clienteHttp.show_addressbook(CONFIG.SERVER_URL, api);
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
                    addressess = this.address;
                    if (addressess != null) {
                        adapter = new AddressAdapter(getApplicationContext(), addressess);
                        Log.e("ARRAYLIST", String.valueOf(addressess.get(0)));
                        AddressList.setAdapter(adapter);
                        address_add.attachToListView(AddressList);
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressBook.this);
                        alertDialog.setTitle("Alertas");
                        alertDialog.setMessage("No Direcciones registradas");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                default:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
            }


        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }
}
