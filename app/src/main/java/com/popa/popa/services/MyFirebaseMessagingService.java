package com.popa.popa.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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
import com.popa.popa.R;
import com.popa.popa.model.Pet;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TOPIC = "battleground";
    private static final String LEGACY_SERVER_KEY = "AIzaSyDkBWHD94tzuf1hodJfaY66mzwTVX0Tgzw";
    private static final String OK = "OK";
    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    private Activity activity;
    private SharedPreferences sp;
    private SharedPreferences tempSp;
    private SharedPreferences.Editor editor;
    private Pet pet;

    public MyFirebaseMessagingService(Activity a){

        activity = a;

        sp = activity.getApplicationContext().getSharedPreferences("Pet", MODE_PRIVATE);
        tempSp = activity.getApplicationContext().getSharedPreferences("temp", MODE_PRIVATE);

        subscribeToTopic();
    }

    public MyFirebaseMessagingService(){

    }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Topic Subscription: completed");
            }
        });
    }

    public void sendNotification(String url, JSONObject data, Context context){
        RequestQueue requstQueue = Volley.newRequestQueue(context);

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String httpBody = remoteMessage.getNotification().getBody();

        System.out.println("httpBody: " + httpBody);

        if(httpBody.equals(OK)){
            comparePetStats();
        } else {
            splitRemoteStats(httpBody);
            sendNotification(URL, getSendingData(pet, true), getApplicationContext());
        }
    }

    private void splitRemoteStats(String httpBody){
        String[] remoteStats = httpBody.split("/");
        int totalRemote = calcTotalRemote(remoteStats);
        storeRemoteStatsTemporarily(totalRemote);
    }

    private void comparePetStats(){

        int totalRemote = getRemoteStatsTemporarily();
        int totalMyself = calcTotalMyself();

        System.out.println("MyFireBaseMessagingService: Remote Total " + totalRemote);
        System.out.println("MyFireBaseMessagingService: My Total " + totalMyself);

        createPushNotification(totalRemote, totalMyself);
    }

    private void createPushNotification(int remoteTotal, int myTotal){
        String body;

        if(remoteTotal > myTotal){
            body = "You lost the fight!";
        } else {
            body = "You won the fight!";
        }

        int notifyID = 1;

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "test";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Fight Result")
                .setContentText(body)
                .setSmallIcon(R.drawable.gunter)
                .setChannelId(CHANNEL_ID)
                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager.notify(notifyID , notification);
    }

    private int calcTotalRemote(String[] remoteStats){
        int totalRemote = 0;
        for(int i = 0; i < remoteStats.length - 1; ++i){
            totalRemote += Integer.parseInt(remoteStats[i]);
        }
        return totalRemote;
    }

    private int calcTotalMyself(){

        SharedPreferences petPrefs = this.getApplicationContext().getSharedPreferences("Pet", MODE_PRIVATE);
        Pet pet =  new Pet(petPrefs.getString("name", "unknown"),petPrefs.getInt("str", 0),petPrefs.getInt("dex",0 ),petPrefs.getInt("agi",0),petPrefs.getInt("intel",0 ),petPrefs.getString("primaryAttribute","unknown"));

        int totalMyself = pet.getStr() + pet.getAgi() + pet.getDex() + pet.getIntel();
        return totalMyself;
    }

    public JSONObject getSendingData(Pet pet, boolean ack){

        this.pet = pet;

        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();
        try{

            if(ack) {
                jsonData.put("body", OK);
            } else {
                jsonData.put("body", "" + pet.getStr() + "/" + pet.getAgi() + "/" + pet.getDex() + "/" + pet.getIntel() + "/" + pet.getPrimaryAttribute());
            }
            jsonData.put("title", "petStats");
            json.put("to", "/topics/battleground");
            json.put("notification", jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Loads a pet into the SharedPreferences of the Android device with all its stats.
     *
     * @param pet - Pet Object with all its stats
     */
    public void storePetInSharedPreferences(Pet pet){

        editor = sp.edit();
        editor.putString("name", pet.getName());
        editor.putInt("str", pet.getStr());
        editor.putInt("dex", pet.getDex());
        editor.putInt("agi", pet.getAgi());
        editor.putInt("intel", pet.getIntel());
        editor.putString("primaryAttribute", pet.getPrimaryAttribute());

        editor.commit();

    }

    /**
     * Returns the pet which is stored in the SharedPreferences of the Android Device.
     *
     * @return Pet Object - the stored Pet with its stats
     */
    public Pet getPetFromSharedPreferences(){

        editor = sp.edit();

        if(sp.contains("name")){
            return new Pet(sp.getString("name", "unknown"),sp.getInt("str", 0),sp.getInt("dex",0 ),sp.getInt("agi",0),sp.getInt("intel",0 ),sp.getString("primaryAttribute","unknown"));
        }

        return new Pet("Gunter", 10, 10, 10, 10, "dex");
    }

    public void storeRemoteStatsTemporarily(int remoteTotal){

        SharedPreferences.Editor meinEditor;
        SharedPreferences meineSharedPreferences = getApplicationContext().getSharedPreferences("temp", MODE_PRIVATE);

        meinEditor = meineSharedPreferences.edit();
        meinEditor.putInt("remoteTotal", remoteTotal);
        meinEditor.commit();
    }

    public int getRemoteStatsTemporarily(){

        SharedPreferences meineSharedPreferences = getApplicationContext().getSharedPreferences("temp", MODE_PRIVATE);

        if(meineSharedPreferences.contains("remoteTotal")){
            return meineSharedPreferences.getInt("remoteTotal", 0);
        }
        return 0;
    }
}
