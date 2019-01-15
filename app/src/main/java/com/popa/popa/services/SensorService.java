package com.popa.popa.services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.AccelerometerBosch;
import com.mbientlab.metawear.module.Led;
import com.mbientlab.metawear.module.Haptic;
import com.mbientlab.metawear.module.AccelerometerBmi160;
import com.mbientlab.metawear.module.AccelerometerBmi160.OutputDataRate;
import com.popa.popa.database.popaDatabase;
import com.popa.popa.model.PostureData;

import java.util.Calendar;

import bolts.Continuation;
import bolts.Task;

/**
 * The Sensor Service offers all methods to interact with the mbientlab sensor.
 */
public class SensorService implements ServiceConnection {

    private final String SENSOR_MAC_ADDRESS_STICK_ON = "E0:CE:8F:84:D5:4F";
    private final String SENSOR_MAC_ADDRESS_CLIP_ON = "C6:9B:12:3C:59:02";

    private BtleService.LocalBinder serviceBinder;
    private Context context;
    private MetaWearBoard board;
    private Led led;
    private AccelerometerBmi160 acc;
    private popaDatabase database;

    //Posture evaluation
    private int evaluateCounter = 0;
    private boolean initPosture = true;
    private float initX;
    private float xTreshold;

    public SensorService(Context ctxt){
        this.context = ctxt;
        database = popaDatabase.getDatabase(context);
        context.bindService(new Intent(context, BtleService.class), this, Context.BIND_AUTO_CREATE);
        context.startService(new Intent(context, BtleService.class));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        serviceBinder = (BtleService.LocalBinder) service;
        retrieveBoard();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }

    /**
     * Tries to build up a connection to the given MAC-Address of the mbientlab sensor via Bluetooth.
     * When the connection is open, the gyroscope send with a frequency of 200 Hz data of the sensor.
     */
    public void retrieveBoard() {
        final BluetoothManager btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice = btManager.getAdapter().getRemoteDevice(SENSOR_MAC_ADDRESS_STICK_ON);

        board = serviceBinder.getMetaWearBoard(remoteDevice);

        board.connectAsync().onSuccessTask(task -> {

                acc = board.getModule(AccelerometerBmi160.class);
                acc.configure()
                        .odr(OutputDataRate.ODR_50_HZ)
                        .range(AccelerometerBosch.AccRange.AR_2G)
                        .commit();

                return acc.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {

                                float x = data.value(Acceleration.class).x();
                                String ts = data.formattedTimestamp();

                                Log.d("SensorData: ", "X: " + x + " , Timestamp: " + ts);

                                if(initPosture){
                                    initializePosture(x);
                                }
                                evaluatePosition(x);
                                    persist(x, ts);
                            }
                        });
                    }
                });
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                if(task.isFaulted()){
                    Log.d("SensorService: ", "Failed to connect");
                    return null;
                } else {
                    acc.start();
                    acc.acceleration().start();
                    playLed();
                    Log.d("SensorService: ", "Connected");
                }
                return null;
            }
        });
    }

    /**
     * Close the connection to the mbientlab Sensor
     */
    public void disconnectSensor(){
        board.disconnectAsync().continueWith(task -> {
            Log.d("SensorService: ", "Disconnected");
            return null;
        });
    }

    /**
     * Gets the Led Module from the sensor and lights up green 3 times.
     */
    private void playLed(){
        led = board.getModule(Led.class);
        led.editPattern(Led.Color.GREEN);
        led.play();
    }

    /**
     * This method evaluates on every reaction of the sensor if the position changed by more than 25%.
     */
    private void evaluatePosition(float x){
        if(evaluateCounter == 300){
            vibrate();
            evaluateCounter = 0;
        } else {
            if(x < (initX - xTreshold)){
                evaluateCounter++;
            }
        }
    }

    /**
     * Initializes the posture of the patient with the x axis.
     * @param x - X-Axis
     */
    private void initializePosture(float x){
        initPosture = false;
        initX = x;
        xTreshold = (initX / 100) * 25;
    }

    /**
     * Vibrate the board.
     */
    private void vibrate(){
        board.getModule(Haptic.class).startBuzzer((short) 500);
    }

    /**
     * On every sensor reaction the persist method hands the data to the DBController to persist the data in the RPL.
     *
     * @param x - X-Axis
     * @param timestamp - Timestamp of the recorded data
     */
    private void persist(float x, String timestamp){
        boolean goodPosture;

        if(x < (initX - xTreshold)){
            goodPosture = false;
        } else {
            goodPosture = true;
        }

        new Thread(() -> {
            PostureData pd = new PostureData();
            pd.setX(x);
            pd.setTimestamp(Calendar.getInstance().getTime());
            pd.setPosture(goodPosture);
            database.daoAccess().insertPostureData(pd);
        }).start();
    }
}
