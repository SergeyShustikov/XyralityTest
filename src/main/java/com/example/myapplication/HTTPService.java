package com.example.myapplication;

import android.content.res.Configuration;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Sergey Shustikov
 *         (pandarium.shustikov@gmail.com)
 */
public class HTTPService {

    private final static String GET_WORLDS_URL = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";
    private String LOGIN_KEY = "login";
    private String PASSWORD_KEY = "password";
    private String DEVICE_TYPE_KEY = "deviceType";
    private String DEVICE_ID_KEY = "deviceId";

    public void getWorlds(final String login, final String password, final String deviceType, final String deviceId, final WorldsParseCallback worldsParseCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<World> worlds = null;
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(GET_WORLDS_URL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair(LOGIN_KEY, login));
                    params.add(new BasicNameValuePair(PASSWORD_KEY, password));
                    params.add(new BasicNameValuePair(DEVICE_TYPE_KEY, deviceType));
                    params.add(new BasicNameValuePair(DEVICE_ID_KEY, deviceId));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse responsePOST = client.execute(post);
                    HttpEntity resEntity = responsePOST.getEntity();
                    if (resEntity != null) {
                        String msg = EntityUtils.toString(resEntity, HTTP.UTF_8);
                        WorldRequest worldRequest = null;
                        try {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.disableHtmlEscaping();
                            gsonBuilder.disableInnerClassSerialization();
                            Gson gson = gsonBuilder.create();
                            worldRequest = gson.fromJson(msg, WorldRequest.class);
                            worlds = worldRequest.allAvailableWorlds;
                        } catch (JsonSyntaxException e) {
                            Log.e("Error", e.getMessage());
                            WorldsParser parser = new WorldsParser(msg);
                            worlds = parser.parse();
                        }
                        Log.i("RESPONSE", msg);
                        if (!msg.contains("error")) {
                            worldsParseCallback.onComplete(worlds);
                        } else {
                            worldsParseCallback.onError(null);
                        }
                        return;
                    }
                } catch (Exception e) {
                    worldsParseCallback.onError(e);
                    e.printStackTrace();
                    return;
                }
                worldsParseCallback.onError(null);
            }
        }
        ).start();
    }

    public static HttpResponse makeRequest(String uri, String json) {

        return null;
    }
}
