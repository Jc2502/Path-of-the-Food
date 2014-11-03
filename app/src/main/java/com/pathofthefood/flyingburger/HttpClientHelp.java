package com.pathofthefood.flyingburger;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HttpClientHelp {

    //private Gson gson = new GsonBuilder().setDateFormat(DATEF).create();

    public static JSONObject Login(String URL, String acUser, String acPass) throws JSONException {
        BufferedReader bufferedReader = null;
        JSONObject jsonObject;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        Log.e("ERROR", "request");
        HttpPost request = new HttpPost(URL +CONFIG.LOGIN);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(CONFIG.USER, acUser));
        postParameters.add(new BasicNameValuePair(CONFIG.PASS, acPass));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);

            bufferedReader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            bufferedReader.close();

            jsonObject = new JSONObject(stringBuffer.toString());

            return jsonObject;

        } catch (ClientProtocolException e) {

            e.printStackTrace();
            Log.d("ClientProtocolException", e.toString());

        } catch (IOException e) {

            e.printStackTrace();

            Log.d("Exception", e.toString());

        } finally {
            if (bufferedReader != null) {
                try {

                    bufferedReader.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }
        return null;
    }

    public static JSONObject logout(String URL, String api)
            throws JSONException {
        BufferedReader bufferedReader = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL+CONFIG.LOGOUT);
        request.setHeader(CONFIG.LOGOUT_HEADER, api);
        Log.e("HEADER-->", api);
        try {
            HttpResponse response = httpClient.execute(request);
            bufferedReader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            bufferedReader.close();
            JSONObject jsonObj = new JSONObject(stringBuffer.toString());
            Log.e("Logout", jsonObj.toString());
            return jsonObj;
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if (bufferedReader != null) {
                try {

                    bufferedReader.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }
        return null;
    }
}
