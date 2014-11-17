package com.pathofthefood.flyingburger.Restaurant;

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
import android.widget.*;
import com.melnykov.fab.FloatingActionButton;
import com.pathofthefood.flyingburger.Address.Address;
import com.pathofthefood.flyingburger.Address.NewAddress;
import com.pathofthefood.flyingburger.*;
import com.pathofthefood.flyingburger.Menu.Home;
import com.pathofthefood.flyingburger.Menu.ShoppingCartActivity;
import com.pathofthefood.flyingburger.ldrawer_library.ActionBarDrawerToggle;
import com.pathofthefood.flyingburger.ldrawer_library.DrawerArrowDrawable;
import com.pathofthefood.flyingburger.utils.SessionManager;
import org.json.JSONException;

import java.util.ArrayList;

public class RestaurantAddressBook extends Activity implements AdapterView.OnItemClickListener {


    View v1;
    TextView tvu, tvm;
    private ListView AddressList;
    private ArrayList<Restaurants> addressess;
    private RestaurantAdapter adapter;
    private SessionManager session;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private DrawerLayout drawer;
    private ListView leftDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private String[] leftSliderData = {"Home", "Android", "Tech Zone", "Sitemap", "About", "Contact Me"};
    private String[] mStrings={
            "http://androidexample.com/media/webservice/LazyListView_images/image0.png",
            "http://www.lowcarbonthetown.com/wp-content/uploads/2013/10/chillis-logo.jpg",
            "http://1.bp.blogspot.com/-XfSpA9cgYPM/UkwH5PfBbAI/AAAAAAAAAEs/l4DTj5c7rvo/s1600/barezzito-tuxtla.jpg",
            "http://www.atumesa.com/m/logosgrandes/318.jpg",
            "http://www.ur.mx/Portals/48/fac.jpg",
            "http://dq7ihshakxjvg.cloudfront.net/photos/restaurant_logo/5951/mediumjpg",
            "http://www.mburgerchicago.com/mburger3.png",
            "http://www.quecomer.com/monterrey/wp-content/uploads/2008/11/n528305750_3284024_4264932.jpg"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantaddressbook);
        session = new SessionManager(getApplicationContext());
        AddressList = (ListView) findViewById(R.id.ListViewAddress);
        new AddressTask(getApplicationContext(), addressess, session.getUserDetails().getApi_token()).execute();
        AddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = (addressess.get(position).getId()).toString();

                startActivity(new Intent(getApplicationContext(), Home.class));


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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        drawer.closeDrawers();
        Toast.makeText(RestaurantAddressBook.this,String.valueOf(leftSliderData[position])+ "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    protected void onRestart() {

        super.onRestart();
        this.onCreate(null);
    }
    public View.OnClickListener listener=new View.OnClickListener(){
        @Override
        public void onClick(View arg0) {

            //Refresh cache directory downloaded images
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
};


    public void onItemClick(int mPosition)
    {
        String tempValues = mStrings[mPosition];

        Toast.makeText(RestaurantAddressBook.this,
                "Image URL : "+tempValues,
                Toast.LENGTH_LONG)
                .show();
    }
    @Override
    public void onDestroy()
    {
        // Remove adapter refference from list
        AddressList.setAdapter(null);
        super.onDestroy();
    }
    class AddressTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private ArrayList<Restaurants> address;
        private String api;

        public AddressTask(Context context, ArrayList<Restaurants> address, String api) {
            this.context = context;
            this.address = address;
            this.api = api;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                if (session.isLoggedIn() && CONFIG.isOnline(this.context)) {
                    HttpClientHelp clienteHttp = new HttpClientHelp();
                    this.address = clienteHttp.show_restaurants(CONFIG.SERVER_URL, api);
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
                    adapter = new RestaurantAdapter(getApplicationContext(), addressess,mStrings);
                    Log.e("ARRAYLIST", String.valueOf(addressess.get(0)));
                    adapter.notifyDataSetChanged();
                    AddressList.setAdapter(adapter);

                    break;
                case CONFIG.ERROR_NOT_AUTH:
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;
                case CONFIG.ERROR_NULL:
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantAddressBook.this);
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
