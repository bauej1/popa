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

public class HeartActivity extends WearableActivity  implements SensorEventListener {

    private static final String TAG = "HeartActivity";
    private TextView text_bpm;
    private Button btnStart;
    SensorManager mSensorManager;
    Sensor mHeartRateSensor;

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
                startMeasure();
                Log.i(TAG, "LISTENER REGISTERED.");
                text_bpm.setText("Please wait...");
            }
        });

        // Enables Always-on
        setAmbientEnabled();
        }
    private void startMeasure() {
        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onResume(){
        super.onResume();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "" + (int)event.values[0];
            text_bpm.setText(msg);
            Log.d(TAG, msg);
        }
        else
            Log.d(TAG, "Unknown sensor type");
    }

}
