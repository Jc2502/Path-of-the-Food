package com.pathofthefood.flyingburger.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.pathofthefood.flyingburger.User;

/**
 * Created by co_mmsalinas on 12/11/2014.
 */
@SuppressLint("CommitPrefEdits")
public class SessionManager {
    //Declarando constantes
    //private String id;
    public static final String KEY_NAME = "fullname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_API = "api_token";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String PREF_NAME = "pofPref";
    public static String KEY_EMAIL = "email";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, user.getFullname());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_API, user.getApi_token());
        editor.commit();
    }

    public User getUserDetails() {
        User user = new User();
        user.setFullname(pref.getString(KEY_NAME, null));
        user.setApi_token(pref.getString(KEY_API, null));
        user.setEmail(pref.getString(KEY_EMAIL, null));
        user.setUsername(pref.getString(KEY_USERNAME, null));
        return user;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
