package com.example.popawear;

import android.content.Context;
import android.content.Intent;
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
 * Controller for the Homeview of the Smartwatch
 */

public class HomeActivity extends WearableActivity implements SensorEventListener {

    private static final String TAG = "HeartActivity";
    String datapath = "/message_path";

    DataContainer dataContainer = DataContainer.getInstance();
    Intent intent = null;
    private TextView stepCount;
    private TextView heartFrequency;
    private Button button_diary;
    private Button button_heartFrequency;
    private SensorManager mSensorManager;
    private Sensor mSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        stepCount = (TextView) findViewById(R.id.text_stepCount);
        heartFrequency = (TextView) findViewById(R.id.text_heart);
        button_diary = findViewById(R.id.button_diary);
        button_heartFrequency = findViewById(R.id.button_heart);
        setStepCounter();

        button_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        button_heartFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), HeartActivity.class);
                startActivity(intent);
            }
        });
    }
    //Method that registers the StepCounter sensor.
    public void setStepCounter(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    public void onSensorChanged( SensorEvent sensorEvent){
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            String msg = ""+(int)sensorEvent.values[0];
            stepCount.setText(msg);
            String message = "S"+(int)sensorEvent.values[0];
            dataContainer.setStepsValue((int)sensorEvent.values[0]);
            new SendThread(datapath, message).start();
            if(dataContainer.getHeartValue() == 0){
                heartFrequency.setText("N/A");

            }else {
                heartFrequency.setText("" + dataContainer.getHeartValue());
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

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
                            Wearable.getMessageClient(HomeActivity.this).sendMessage(node.getId(), path, message.getBytes());

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
};
