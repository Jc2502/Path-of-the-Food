package com.pathofthefood.flyingburger;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class CONFIG {
    public static final String SERVER_URL = "http://pof.marinsalinas.com/api/v1/";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String API_HEADER = "X-Auth-Token";
    public static final String USR_INFO = "users";
    public static final String USER = "username";
    public static final String PASS = "password";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String FN = "fullname";
    public static final String ADDRESS = "addressbook";
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String TXTADDRESS = "textaddress";
    public static final String LAT = "latitude";
    public static final String LON = "longitude";
    public static final String RESTAURANT = "restaurants";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int ERROR_JSON = 0;
    public static final int ERROR_NOT_AUTH = 1;
    public static final int ERROR_NULL = 2;

    public static final int DONE = -1;


    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public static void isAuth(HttpResponse response, JSONObject jsonObj) throws JSONException, NotAuthException{
        //Si el Status Code es diferente de 200 mandamos el error
        if (response.getStatusLine().getStatusCode() != 200) {
            if (jsonObj.getBoolean("error")) {
                String errorMsj = jsonObj.getString("message");
                if (errorMsj.equals("No Autenticado")) {
                    throw new NotAuthException(errorMsj, true);
                }
            }

        }
    }

}
