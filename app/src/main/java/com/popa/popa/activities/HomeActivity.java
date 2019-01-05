package com.popa.popa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.popa.popa.R;
import com.popa.popa.model.StepDetector;
import com.popa.popa.model.StepListener;
import com.popa.popa.services.MessageReceiver;

public class HomeActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    //2 Variables used for data message transfer from Smartwatch
    private static final String URL = "https://fcm.googleapis.com/fcm/send";
    String datapath = "/message_path";


    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    ImageButton bSettings;
    ImageButton bStatistics;
    ImageButton bPet;
    ImageButton bInformation;
    ImageButton bDiary;
    ImageButton bHeart;
    ImageButton bSteps;
    TextView tHeart;
    TextView tSteps;
    Bundle bundle;
    String heartRate;
    String steps;
    Boolean watchConnected = false;

    SharedPreferences sp;
    SharedPreferences spHeart;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        HomeActivity.MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

        sp = this.getApplicationContext().getSharedPreferences("user", 0);


        getIntentBundle(getIntent());

        // Checks whether the bundle is packed or not
        if(bundle == null){
            // If bundle is null, the data from user is chosen from the mobile devise
            editor = sp.edit();
            Bundle bundleIfNull = new Bundle();
            bundleIfNull.putString("gender", sp.getString("gender", ""));
            bundleIfNull.putString("age", sp.getString("age", ""));

            this.bundle = bundleIfNull;
        } else {
            // If bundle is not null, the data is chosen from the bundle
            editor = sp.edit();
            editor.putString("gender", bundle.getString("gender"));
            editor.putString("age", bundle.getString("age"));
            editor.commit();
        }


        tHeart = findViewById(R.id.tHeart);
        bSettings = findViewById(R.id.bSettings);
        bStatistics = findViewById(R.id.bStatistics);
        bPet = findViewById(R.id.bPet);
        bInformation = findViewById(R.id.bInfo);
        bDiary = findViewById(R.id.bDiary);
        bHeart = findViewById(R.id.bHeart);
        tSteps = findViewById(R.id.tSteps);
        bSteps = findViewById(R.id.bSteps);



        bHeart.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HeartRateMonitor.class);
            startActivity(intent);
        });

        // Opens SettingsActivity if Settings button is clicked
        bSettings.setOnClickListener(v -> {
            Intent a = new Intent(v.getContext(), SettingsActivity.class);
            a.putExtras(bundle);
            startActivity(a);
        });

        // Opens StatisticsActivity if Statistics button is clicked
        bStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), StatisticsActivity.class);
            startActivity(intent);
        });

        // Opens PetActivity if Pet button is clicked
        bPet.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PetActivity.class);
            startActivity(intent);
        });

        // Opens InformationActivity if Information button is clicked
        bInformation.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InformationActivity.class);
            startActivity(intent);
        });

        // Opens DiaryActivity if Diary button is clicked
        bDiary.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DiaryActivity.class);
            startActivity(intent);
        });

        if(!watchConnected) {
            Log.d("Stepper", "Sensor active");
            sensorManager.registerListener(HomeActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        }else{

        }

        bSteps.setOnClickListener(v -> {
            Log.d("Stepper","Sensor active");
                numSteps = 0;
                sensorManager.registerListener(HomeActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        spHeart = this.getApplicationContext().getSharedPreferences("heartRate", 0);
        heartRate = spHeart.getString("rate", "N/A");
        steps = spHeart.getString("step", "N/A");
        if(!heartRate.equals("N/A")){
            tHeart.setText(heartRate);
        }else{
            tHeart.setText("--");
        }
        if(!steps.equals("N/A")){
            tSteps.setText(steps);
        }else{
            tSteps.setText("--");
        }
    }

    private void getIntentBundle(Intent intent){
        bundle = intent.getExtras();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        numSteps = Integer.valueOf(spHeart.getString("step","0"));
        numSteps++;
        tSteps.setText(spHeart.getString("step", "N/A"));
        editor = spHeart.edit();
        editor.putString("step",""+numSteps);
        editor.commit();
    }

    /////////////////////////////////////////////// Added the Receiver for the Smart Watch ///////////////////////////////////////////////////////
//setup a broadcast receiver to receive the messages from the wear device via the listenerService.
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            watchConnected = true;
            String message = intent.getStringExtra("message");
            Log.d( "becho ", message);
            if(message.charAt(0) == 'S'){
                loggingSteps(message);
            }else{
                logging(message);
            }
        }
        // simple method to add the log TextView.
        public void logging(String message) {
            tHeart.setText(message);
            editor = spHeart.edit();
            editor.putString("rate",message);
            editor.commit();
        }
        public void loggingSteps(String message){
            String steps = message.substring(1);
            editor = spHeart.edit();
            editor.putString("step",steps);
            editor.commit();
            tSteps.setText(spHeart.getString("step", "N/A"));
        }
    }
}
