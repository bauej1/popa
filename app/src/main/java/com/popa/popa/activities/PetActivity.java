package com.popa.popa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.popa.popa.R;
import com.popa.popa.model.Pet;
import com.popa.popa.services.MyFirebaseMessagingService;
import com.popa.popa.services.PetService;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetActivity extends AppCompatActivity implements
        View.OnClickListener  {

    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    private MyFirebaseMessagingService messagingService;

    String datapath = "/message_path";
    Button mybutton;
    TextView logger;
    protected Handler handler;
    String TAG = "Mobile MainActivity";
    int num = 1;

    //Stat Controls
    private TextView tStrPetValue;
    private TextView tDexPetValue;
    private TextView tAgiPetValue;
    private TextView tIntPetValue;
    private TextView tPetName;
    private String primaryAttribute;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        //get the widgets
        mybutton = findViewById(R.id.button_startBattle);
        mybutton.setOnClickListener(this);
        logger = findViewById(R.id.text_petName);

        //Stat Controls initializing
        tStrPetValue = findViewById(R.id.text_PetStr);
        tDexPetValue = findViewById(R.id.text_PetDex);
        tAgiPetValue = findViewById(R.id.text_PetAgi);
        tIntPetValue = findViewById(R.id.text_PetInt);
        tPetName = findViewById(R.id.text_petName);

        //message handler for the send thread.
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                logthis(stuff.getString("logthis"));
                return true;
            }
        });

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);


        tIntPetValue.setText("40");

        messagingService = new MyFirebaseMessagingService(this);
        getPetFromSharedPreferences();


    }

    /*
     * simple method to add the log TextView.
     */
    public void logthis(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            logger.append("\n" + newinfo);
        }
    }

    //setup a broadcast receiver to receive the messages from the wear device via the listenerService.
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.v(TAG, "Main activity received message: " + message);
            // Display message in UI
            logthis(message);

        }
    }

    //button listener
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_startBattle:
                messagingService.sendNotification(URL, messagingService.getSendingData(pet, false), getApplicationContext());
                break;
        }

//        String message = "Hello wearable " + num;
//        //Requires a new thread to avoid blocking the UI
//        new SendThread(datapath, message).start();
//        num++;
    }

    @Override
    protected void onStop(){
        super.onStop();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("battleground");
        System.out.println("BattleMode: unsubscribed from battleground firebase topic");

        getPetStats();
        messagingService.storePetInSharedPreferences(pet);
    }

    //method to create up a bundle to send to a handler via the thread below.
    public void sendmessage(String logthis) {
        Bundle b = new Bundle();
        b.putString("logthis", logthis);
        Message msg = handler.obtainMessage();
        msg.setData(b);
        msg.arg1 = 1;
        msg.what = 1; //so the empty message is not used!
        handler.sendMessage(msg);

    }

    //This actually sends the message to the wearable device.
    class SendThread extends Thread {
        String path;
        String message;

        //constructor
        SendThread(String p, String msg) {
            path = p;
            message = msg;
        }

        //sends the message via the thread.  this will send to all wearables connected, but
        //since there is (should only?) be one, no problem.
        public void run() {

            //first get all the nodes, ie connected wearable devices.
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                // Block on a task and get the result synchronously (because this is on a background
                // thread).
                List<Node> nodes = Tasks.await(nodeListTask);

                //Now send the message to each device.
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(PetActivity.this).sendMessage(node.getId(), path, message.getBytes());

                    try {
                        // Block on a task and get the result synchronously (because this is on a background
                        // thread).
                        Integer result = Tasks.await(sendMessageTask);
                        sendmessage("SendThread: message send to " + node.getDisplayName());
                        Log.v(TAG, "SendThread: message send to " + node.getDisplayName());

                    } catch (ExecutionException exception) {
                        sendmessage("SendThread: message failed to" + node.getDisplayName());
                        Log.e(TAG, "Send Task failed: " + exception);

                    } catch (InterruptedException exception) {
                        Log.e(TAG, "Send Interrupt occurred: " + exception);
                    }

                }

            } catch (ExecutionException exception) {
                sendmessage("Node Task failed: " + exception);
                Log.e(TAG, "Node Task failed: " + exception);

            } catch (InterruptedException exception) {
                Log.e(TAG, "Node Interrupt occurred: " + exception);
            }

        }
    }

    private void getPetFromSharedPreferences(){
        pet = messagingService.getPetFromSharedPreferences();
        setPetStats();
    }

    private void setPetStats(){
        tPetName.setText(pet.getName());
        tStrPetValue.setText(pet.getStr() + "");
        tDexPetValue.setText(pet.getDex() + "");
        tAgiPetValue.setText(pet.getAgi() + "");
        tIntPetValue.setText(pet.getIntel() + "");
    }

    private void getPetStats(){
        pet.setStr(Integer.parseInt(tStrPetValue.getText() +""));
        pet.setDex(Integer.parseInt(tDexPetValue.getText() +""));
        pet.setAgi(Integer.parseInt(tAgiPetValue.getText() +""));
        pet.setIntel(Integer.parseInt(tIntPetValue.getText() +""));
    }
}
