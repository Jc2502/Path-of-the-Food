package com.pathofthefood.flyingburger;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
