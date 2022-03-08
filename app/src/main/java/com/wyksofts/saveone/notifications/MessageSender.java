package com.wyksofts.saveone.notifications;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wyksofts.saveone.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageSender {

    public void sendNotification() {
        requestQueue = Volley.newRequestQueue(activity);
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("to", userFCMToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body",body);
            notiObject.put("icon", R.mipmap.ic_launcher_round);


            jsonObject.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                public Map<String, String> getHeaders() {

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key="+ServerKey);

                    return header;
                }
            };

            requestQueue.add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public MessageSender(String userFCMToken, String title, String body, Context context, Activity activity) {
        this.userFCMToken = userFCMToken;
        this.title = title;
        this.body = body;
        this.context = context;
        this.activity = activity;
    }

    String userFCMToken;
    String title;
    String body;
    Context context;
    Activity activity;

    private  RequestQueue requestQueue;
    private final String postUrl = "https://fcm.gooleapis.com/fcm/send";
    private final String ServerKey = "AAAAzWEwgvI:APA91bFpOguoFBQIn3usfT1z9SV8jZRjYUjrgrCoeMT8qcm4QKnOngO0AIzxF-Nc6ubzxRiuE4wu8vi70VrLGg-LZfKVU2KuUe-0_u7zVEYuuNqTgi522ytyRAnMVyU8cFfgfJLQgUOo";


}
