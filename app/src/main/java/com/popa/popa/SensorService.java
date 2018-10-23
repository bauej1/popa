package com.popa.popa;

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
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.AngularVelocity;
import com.mbientlab.metawear.module.Led;
import com.mbientlab.metawear.module.GyroBmi160;
import com.mbientlab.metawear.module.GyroBmi160.OutputDataRate;
import com.mbientlab.metawear.module.Haptic;

/**
 * The Sensor Service offers all methods to interact with the mbientlab sensor.
 */
public class SensorService implements ServiceConnection {

    private final String SENSOR_MAC_ADDRESS = "E0:CE:8F:84:D5:4F";

    private BtleService.LocalBinder serviceBinder;
    private Context context;
    private MetaWearBoard board;
    private Led led;
    private GyroBmi160 gyro;

    //Posture evaluation
    private int evaluateCounter = 0;
    private boolean initPosture = true;
    private float initZ;
    private float initY;
    private float zTreshold;
    private float yTreshold;

    public SensorService(Context ctxt){
        this.context = ctxt;
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
        final BluetoothDevice remoteDevice = btManager.getAdapter().getRemoteDevice(SENSOR_MAC_ADDRESS);

        board = serviceBinder.getMetaWearBoard(remoteDevice);

        board.connectAsync().onSuccessTask(task -> {
            if (task.isFaulted()) {
                Log.i("SensorService: ", "Failed to connect");
            } else {
                Log.i("SensorService: ", "Connected");
                playLed();

                gyro = board.getModule(GyroBmi160.class);
                gyro.configure()
                        .odr(OutputDataRate.ODR_50_HZ)
                        .commit();

                return gyro.angularVelocity().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {

                                float z = data.value(AngularVelocity.class).z();
                                float y = data.value(AngularVelocity.class).y();

                                if(initPosture){
                                    initializePosture(z, y);
                                    initPosture = false;
                                }
                                evaluatePosition(z, y);
                                Log.i("Gyroscope Sensor: ", data.value(AngularVelocity.class).toString());
                            }
                        });
                    }
                });
            }
            return null;
        });
    }

    /**
     * Close the connection to the mbientlab Sensor
     */
    public void disconnectSensor(){
        board.disconnectAsync().continueWith(task -> {
            Log.i("SensorService: ", "Disconnected");
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
     * This method evaluates on every reaction of the sensor if the position changed by more than 20%.
     */
    private void evaluatePosition(float z, float y){
        if(evaluateCounter == 100){
            vibrate();
            evaluateCounter = 0;
        } else {
            if((z - zTreshold) < initZ && (y - yTreshold) < initY){
                evaluateCounter++;
            }
        }
    }

    /**
     * Initializes the posture of the patient with y and z axis.
     * @param z - Z-Axis
     * @param y - Y-Axis
     */
    private void initializePosture(float z, float y){

        initY = y;
        initZ = z;

        zTreshold = (initZ / 100) * 30;
        yTreshold = (initY / 100) * 30;
    }

    /**
     * Vibrate the board.
     */
    private void vibrate(){
        board.getModule(Haptic.class).startBuzzer((short) 500);
    }
}
