package com.pathofthefood.flyingburger;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

    //Iniciar Sesion
    public static JSONObject Login(String URL, String acUser, String acPass) throws JSONException {
        BufferedReader bufferedReader = null;
        JSONObject jsonObject;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        Log.e("ERROR", "request");
        HttpPost request = new HttpPost(URL + CONFIG.LOGIN);
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

    //Registro de Usuario
    public static JSONObject register(String URL,String user,String pass,String email,String phone,String fullname) throws JSONException {
        BufferedReader bufferedReader = null;
        JSONObject jsonObject;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(URL + CONFIG.USR_INFO);
        Log.e("Request", String.valueOf(request));
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(CONFIG.USER, user));
        postParameters.add(new BasicNameValuePair(CONFIG.PASS, pass));
        postParameters.add(new BasicNameValuePair(CONFIG.EMAIL, email));
        postParameters.add(new BasicNameValuePair(CONFIG.PHONE, phone));
        postParameters.add(new BasicNameValuePair(CONFIG.FN, fullname));

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

    //Cerrar Sesion
    public static JSONObject logout(String URL, String api)
            throws JSONException {
        BufferedReader bufferedReader = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + CONFIG.LOGOUT);
        request.setHeader(CONFIG.API_HEADER, api);
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

    // Obtener informacion de Usuario
    public static JSONObject user_info(String URL, String api)
            throws JSONException {
        BufferedReader bufferedReader = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + CONFIG.USR_INFO);
        request.setHeader(CONFIG.API_HEADER, api);
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

    //Editar la informacion del usuario esta puede ser igual
    public static JSONObject edit_user(String URL,String api,String id, String user,String email,String phone,String fullname) throws JSONException {
        BufferedReader bufferedReader = null;
        JSONObject jsonObject;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //HttpPost request = new HttpPost(URL + CONFIG.USR_INFO+id);
        HttpPut request = new HttpPut(URL+CONFIG.USR_INFO+"/"+id);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(CONFIG.USER, user));
        postParameters.add(new BasicNameValuePair(CONFIG.EMAIL, email));
        postParameters.add(new BasicNameValuePair(CONFIG.PHONE, phone));
        postParameters.add(new BasicNameValuePair(CONFIG.FN, fullname));

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

    //Obtener Direcciones
    public static JSONObject show_addressbook(String URL, String api)
            throws JSONException {
        BufferedReader bufferedReader = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + CONFIG.ADDRESS);
        request.setHeader(CONFIG.API_HEADER, api);
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
            Log.e("Addresses", jsonObj.toString());
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

    //Obtener Direccion especifica
    public static JSONObject show_address(String URL, String api,String id)
            throws JSONException {
        BufferedReader bufferedReader = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + CONFIG.ADDRESS+"/"+id);
        request.setHeader(CONFIG.API_HEADER, api);
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
            Log.e("Address", jsonObj.toString());
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

    //Editar Direccion Especifica
    public static JSONObject edit_address(String URL,String api,String id, String label,String description,String textaddress, String latitude,String longitude) throws JSONException {
        BufferedReader bufferedReader = null;
        JSONObject jsonObject;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //HttpPost request = new HttpPost(URL + CONFIG.USR_INFO+id);
        HttpPut request = new HttpPut(URL+CONFIG.USR_INFO+"/"+id);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(CONFIG.LABEL, label));
        postParameters.add(new BasicNameValuePair(CONFIG.DESCRIPTION, description));
        postParameters.add(new BasicNameValuePair(CONFIG.TXTADDRESS, textaddress));
        postParameters.add(new BasicNameValuePair(CONFIG.LAT, latitude));
        postParameters.add(new BasicNameValuePair(CONFIG.LON, longitude));

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

    //Crear nueva direccion
    public static JSONObject add_address(String URL,String api, String label,String description,String textaddress, String latitude,String longitude) throws JSONException {
        BufferedReader bufferedReader = null;
        JSONObject jsonObject;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        Log.e("ERROR", "request");
        HttpPost request = new HttpPost(URL + CONFIG.ADDRESS);
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(CONFIG.LABEL, label));
        postParameters.add(new BasicNameValuePair(CONFIG.DESCRIPTION, description));
        postParameters.add(new BasicNameValuePair(CONFIG.TXTADDRESS, textaddress));
        postParameters.add(new BasicNameValuePair(CONFIG.LAT, latitude));
        postParameters.add(new BasicNameValuePair(CONFIG.LON, longitude));

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
}
