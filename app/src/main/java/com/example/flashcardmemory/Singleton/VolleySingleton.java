package com.example.flashcardmemory.Singleton;

import android.content.Context;
import android.os.Build;
import android.support.v4.util.Consumer;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashcardmemory.Model.Authentication;
import com.example.flashcardmemory.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VolleySingleton {

    public static final String ERROR_EMAIL = "ERROR_EMAIL";
    private final static String REQUEST_URL = "http://192.168.1.11:8080/";
    private static VolleySingleton instance;
    private static Context ctx;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public void createUser(final User user, final Consumer<User> listener) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        String url = REQUEST_URL + "users";
        final String requestBody = gson.toJson(user);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("VOLLEY_SUCCESS", response.toString());
                User user = gson.fromJson(response.toString(), User.class);
                listener.accept(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.accept(null);
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }
                return null;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getUserByEmail(User user, final Consumer<Authentication> listener) {
        String url = REQUEST_URL + "users/search";
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        final String requestBody = gson.toJson(user);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("VOLLEY_SUCCESS", response.toString());
                Authentication authentication = gson.fromJson(response.toString(), Authentication.class);
                listener.accept(authentication);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.accept(null);
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }
                return null;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
