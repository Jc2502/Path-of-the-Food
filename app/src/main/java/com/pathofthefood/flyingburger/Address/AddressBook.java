package com.pathofthefood.flyingburger.Address;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.*;
import com.pathofthefood.flyingburger.Pedidos.OrdersBook;
import com.pathofthefood.flyingburger.ldrawer_library.ActionBarDrawerToggle;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.GPSTracker;
import com.pathofthefood.flyingburger.utils.HttpClientHelp;
import com.pathofthefood.flyingburger.utils.NotAuthException;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class AddressBook extends Activity implements AdapterView.OnItemClickListener {

    FloatingActionButton address_add;
    View v1;
    TextView tvu, tvm;
    GPSTracker gps;
    private ListView AddressList;
    private ArrayList<Address> addressess;
    private ArrayList<Recomend> recomends;
    private AddressAdapter adapter;
    private SessionManager session;
    private DrawerAdapter navigationDrawerAdapter;
    private DrawerLayout drawer;
    private ListView leftDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private ArrayList<DrawerItem> menu_drawer;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navMenuTitles = getResources().getStringArray(R.array.drawer_text);
        menu_drawer = new ArrayList<DrawerItem>();
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.drawer_items);
        //= new ArrayList<String>(Arrays.asList("Perfil","Pedidos","Historial","Ajustes")
        setContentView(R.layout.activity_addressbook);
        menu_drawer.add(new DrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0,0)));
        menu_drawer.add(new DrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1,0)));
        menu_drawer.add(new DrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2,0)));
        menu_drawer.add(new DrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3,0)));
        session = new SessionManager(getApplicationContext());
        init();
        initDrawer();
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
        //getActionBar().setIcon(new ColorDrawable(Color.TRANSPARENT));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerList = (ListView) findViewById(R.id.list_view_drawer);
        Resources res = getResources();
        int[] images = res.getIntArray(R.array.drawer_items);
        v1 = getLayoutInflater().inflate(R.layout.header, null);
        tvu = (TextView) v1.findViewById(R.id.headeruser);
        tvm = (TextView) v1.findViewById(R.id.headermail);

        tvu.setText(session.getUserDetails().getFullname());
        tvm.setText(session.getUserDetails().getEmail());
        leftDrawerList.addHeaderView(v1);
        navigationDrawerAdapter = new DrawerAdapter(AddressBook.this,menu_drawer);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        drawer.closeDrawers();
        int value = position-1;
        if(value != -1) {
            switch (position){
                case 1:
                    startActivity(new Intent(getApplicationContext(),Information.class));
                    break;
                case 2:
                    startActivity(new Intent(getApplicationContext(),OrdersBook.class).putExtra("history",0));
                    break;
                case 3:
                    Toast.makeText(AddressBook.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(), OrdersBook.class).putExtra("history", 1));
                    break;
                case 4:
                    Toast.makeText(AddressBook.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                    gps = new GPSTracker(AddressBook.this);
                    String lat = String.valueOf(gps.getLatitude());
                    String lon = String.valueOf(gps.getLongitude());
                    new RecomendTask(getApplicationContext(),recomends,session.getUserDetails().getApi_token(),lat,lon).execute();
                    break;
            }

        }
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

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, drawerArrow, R.string.drawer_open, R.string.drawer_close) {

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


    @Override
    protected void onRestart() {

        super.onRestart();
        this.onCreate(null);
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
                    adapter = new AddressAdapter(getApplicationContext(), addressess,AddressBook.this);
                    Log.e("ARRAYLIST", String.valueOf(addressess.get(0)));
                    adapter.notifyDataSetChanged();
                    AddressList.setAdapter(adapter);
                    address_add.attachToListView(AddressList);

                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
                case CONFIG.ERROR_NULL:
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressBook.this);
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

    class RecomendTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private ArrayList<Recomend> recomend;
        private String api,lat,lon;
        JSONArray jsonArray;

        public RecomendTask(Context context, ArrayList<Recomend> recomend, String api,String lat, String lon) {
            this.context = context;
            this.recomend = recomend;
            this.api = api;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                if (session.isLoggedIn() && CONFIG.isOnline(this.context)) {
                    HttpClientHelp clienteHttp = new HttpClientHelp();

                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR);
                    String time = "0";
                    if(hour > 18){
                        time = "2";
                    }
                    else if(hour >12)
                    {
                        time = "1";
                    }

                    jsonArray= clienteHttp.show_recomendation(CONFIG.SERVER_URL, api,lat,lon,time);
                    if (jsonArray == null || jsonArray.length() == 0) {
                        return CONFIG.ERROR_NULL;
                    }
                    return CONFIG.DONE;
                } else if (session.isLoggedIn() && !CONFIG.isOnline(this.context)) {
                    return CONFIG.DONE;
                }
                return CONFIG.ERROR_NOT_AUTH;
            } catch (JSONException e) {
                return CONFIG.ERROR_JSON;
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
                    String mensaje = "";
                    try {
                         mensaje = "Restaurant: "+jsonArray.getJSONObject(0).getJSONObject("restaurant").getString("name");
                         mensaje = mensaje+ "\t\n Producto: "+jsonArray.getJSONObject(0).getJSONObject("menu_item").getString("product");
                         mensaje = mensaje+"\t\n Distancia: "+jsonArray.getJSONObject(0).getString("distance")+" m";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddressBook.this);
                    alert.setTitle("Recomendacion");
                    alert.setMessage(mensaje);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
                case CONFIG.ERROR_NULL:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressBook.this);
                    alertDialog.setTitle("Recomendacion");
                    alertDialog.setMessage("No Existen Recomendaciones Por el Momento");
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
