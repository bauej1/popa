package com.popa.popa.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 *  This is a listener on the  device to get messages via
 *  the datalayer and then pass it to the main activity so it can be
 *  displayed.  the messages should be coming from the wear/watch device.
 *
 *   * @author JimSeeker - https://github.com/JimSeker/wearable
 *
 */
public class ListenerService extends WearableListenerService {

    String TAG = "mobile Listener";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());

            // Broadcast message to MainActivity for display
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }

}