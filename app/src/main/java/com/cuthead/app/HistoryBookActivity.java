package com.cuthead.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cuthead.controller.CustomRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class HistoryBookActivity extends ActionBarActivity {
    private RequestQueue mRequestQueue;

    private static String url =  "http://204.152.218.52/push-notification/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_book);

        mRequestQueue = Volley.newRequestQueue(this);

            final JSONObject json = new JSONObject();
        try {
            json.put("tag", "dingboyang");
            json.put("content", "This is fucking awesome!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
/*
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d("TestVolley",json.toString());
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("tag","dingboyang");
                params.put("content","This is fucking awesome!");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Referer",url);
                return params;
            }
        };

        mRequestQueue.add(req);
*/

        Map<String,String> para = new HashMap<String, String>();
        para.put("tag","dingboyang");
        para.put("content","This is awesome!");

        CustomRequest req = new CustomRequest(Request.Method.POST,url,para,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                VolleyLog.d("TestVolley",jsonObject.toString());
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        mRequestQueue.add(req);






        /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                POST(url);
            }
        });
        thread.start();
        */

    }

    public static String POST(String url){
        InputStream inputStream = null;
        String result = "";


            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";


        try {


            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tag", "dingboyang");
            jsonObject.put("content", "This is fucking awesome!");

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

        } catch (JSONException e){
            e.printStackTrace();
        }

            // 5. set json to StringEntity
        StringEntity se = null;
        try {
            se = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
        try {
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("Http Response:", httpResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 11. return result
        return result;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
