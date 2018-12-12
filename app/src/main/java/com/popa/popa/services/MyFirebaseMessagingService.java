package com.popa.popa.services;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Activity activity;
    private static final String TAG = "MyFMS";
    private static final String TOPIC = "battleground";
    private static final String LEGACY_SERVER_KEY = "AIzaSyDkBWHD94tzuf1hodJfaY66mzwTVX0Tgzw";
    private static final String URL = "https://fcm.googleapis.com/fcm/send";


    public MyFirebaseMessagingService(Activity a){
        this.activity = a;
        subscribeToTopic();
    }

    public MyFirebaseMessagingService(){

    }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Registration on battleground topic was successfull.";
                if(!task.isSuccessful()){
                    msg = "Registration on battleground topic failed!.";
                }
                System.out.println("POPA: subscribe-Status: " + msg);
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                sendNotification(URL, getSendingData());
            }
        });
    }

    public void sendNotification(String url,JSONObject data){
        RequestQueue requstQueue = Volley.newRequestQueue(activity);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("POPA: SERVER RESPONSE: " + response.getString("body"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("POPA: SERVER ERROR: " + error.getMessage());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("content-type","application/json");
                header.put("authorization", "key=" + LEGACY_SERVER_KEY);
                return header;
            }
        };
        requstQueue.add(jsonobj);
    }

    private JSONObject getSendingData(){
        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();
        try{
            jsonData.put("body", "HI THERE!");
            jsonData.put("title", "TITLE FOR HI THERE");
            json.put("to", "/topics/battleground");
            json.put("notification", jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
