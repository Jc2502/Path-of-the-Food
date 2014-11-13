package com.pathofthefood.flyingburger;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pathofthefood.flyingburger.utils.SessionManager;


public class SplashScreen extends Activity {
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   private class PrefetchData extends AsyncTask<Void, Void, Boolean>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           session = new SessionManager(getApplicationContext());
       }

       @Override
       protected Boolean doInBackground(Void... arg0) {

          if(session.isLoggedIn() && CONFIG.isOnline(getApplicationContext())){
              return false;
          }
           return true;
       }

       @Override
       protected void onPostExecute(Boolean result) {
           super.onPostExecute(result);
          /* if(!result) {
               Intent i = new Intent(getApplicationContext(), LoginActivity.class);
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               getApplicationContext().startActivity(i);
           }else{
               //TODO: Si el usuario esta loggeado ver4ificar si tiene clave de notificaciones si no tiene registrar una
               GcmRegManager gcmRegManager = new GcmRegManager(getApplicationContext());
               gcmRegManager.registerGCM();
               Intent i = new Intent(getApplicationContext(), MainActivity.class);
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               getApplicationContext().startActivity(i);
           }
           finish();*/
       }
    }

}
