package com.example.popawear;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class that activates the Sensor to measure the heart frequency
 */

public class HeartActivity extends WearableActivity  implements SensorEventListener {

    private static final String TAG = "HeartActivity";
    DataContainer dataContainer = DataContainer.getInstance();
    private TextView text_bpm;
    private Button btnStart;
    SensorManager mSensorManager;
    Sensor mHeartRateSensor;
    boolean toggler;
    String datapath = "/message_path";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        text_bpm = (TextView) findViewById(R.id.text_bpm);
        btnStart = findViewById(R.id.btnStart);
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
      //  mSensorManager.registerListener(this, mHeartRateSensor, mSensorManager.SENSOR_DELAY_NORMAL);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggler){
                    startMeasure();
                    Log.i(TAG, "LISTENER REGISTERED.");
                    text_bpm.setText("Please wait...");
                }else{
                    stopMeasure();
                }
            }
        });

        // Enables Always-on
        setAmbientEnabled();
        }
        //Method to start (register) the built in heart frequecy sensor.
    private void startMeasure() {
        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        toggler = false;
        btnStart.setText("Stop");
    }
 //Stops the measure and unregisters the sensor, so no new data are being created.
    private void stopMeasure(){
        mSensorManager.unregisterListener(this);
        btnStart.setText("Measure");
        toggler = true;
    }

    public void onResume(){
        super.onResume();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }
    //when new Data from sensor is detected. it will write those into the share preferences and create a message that is being sent to the smartphone
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "H" + (int)event.values[0];
            String frequency = msg.substring(1);
            text_bpm.setText(frequency);
            Log.d(TAG, msg);
            dataContainer.setHeartValue((int)event.values[0]);
            //Requires a new thread to avoid blocking the UI
            new SendThread(datapath, msg).start();
            Log.d("Gsendet",msg);
        }
        else
            Log.d(TAG, "Unknown sensor type");
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
        //since there is (should only?) be one, so no problem.
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
                            Wearable.getMessageClient(HeartActivity.this).sendMessage(node.getId(), path, message.getBytes());

                    try {
                        // Block on a task and get the result synchronously (because this is on a background
                        // thread).
                        Integer result = Tasks.await(sendMessageTask);
                        Log.v(TAG, "SendThread: message send to " + node.getDisplayName());

                    } catch (ExecutionException exception) {
                        Log.e(TAG, "Task failed: " + exception);

                    } catch (InterruptedException exception) {
                        Log.e(TAG, "Interrupt occurred: " + exception);
                    }

                }

            } catch (ExecutionException exception) {
                Log.e(TAG, "Task failed: " + exception);

            } catch (InterruptedException exception) {
                Log.e(TAG, "Interrupt occurred: " + exception);
            }
        }
    }

}
