package com.example.android.constantinenews.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.constantinenews.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by merouane on 15/02/2018.
 */

public class NetworkUtils {


    public static final String ENDPOINT_URL = "http://www.elwatan.com/regions/est/constantine/rss.xml";

    private static String TAG = NetworkUtils.class.toString();


    public static String getRawXML() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ENDPOINT_URL)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
