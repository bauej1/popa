package com.example.popawear;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends WearableActivity implements SensorEventListener {
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
            dataContainer.setStepsValue((int)sensorEvent.values[0]);
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
};
