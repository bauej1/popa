package com.popa.popa;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.android.BtleService;

import bolts.Continuation;
import bolts.Task;

public class SensorService implements ServiceConnection {

    private final String SENSOR_MAC_ADDRESS = "E0:CE:8F:84:D5:4F";

    private BtleService.LocalBinder serviceBinder;
    private Context context;
    private MetaWearBoard board;

    public SensorService(Context ctxt){
        this.context = ctxt;
    }

    public void attachService(){
        context.bindService(new Intent(context, BtleService.class), this, Context.BIND_AUTO_CREATE);
    }

    //maybe not needed..
    public void removeService(){
        context.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        serviceBinder = (BtleService.LocalBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }

    public void retrieveBoard(){
        final BluetoothManager btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice = btManager.getAdapter().getRemoteDevice(SENSOR_MAC_ADDRESS);

        board = serviceBinder.getMetaWearBoard(remoteDevice);

        //Replace with lambda expression!
        board.connectAsync().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                if (task.isFaulted()) {
                    Log.i("SensorService: ", "Failed to connect");
                } else {
                    Log.i("SensorService: ", "Connected");
                }
                return null;
            }
        });
    }

    public void disconnectSensor(){
        board.disconnectAsync().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Log.i("SensorService: ", "Disconnected");
                return null;
            }
        });
    }
}
