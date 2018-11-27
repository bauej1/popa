package com.example.popawear;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/*
This is the Listener on the mobile device to get message via Datalayer.
Dataflow: Device --> wearable
 */

public class ListenerService extends WearableListenerService {
    String TAG ="wear listener";
    @Override
    public void onMessageReceived (MessageEvent messageEvent) {
        if(messageEvent.getPath().equals("/message_path")){
            final String message = new String(messageEvent.getData());
            Log.v(TAG, "Message path received is: "+ messageEvent.getPath());
            Log.v(TAG, "Message received is: "+ message);

            //Broadcas message to wearable activity for display
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else{
            super.onMessageReceived(messageEvent);
        }
    };
}
