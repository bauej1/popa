package com.popa.popa.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class HomeActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    private static int CALLBACK_NUMBER = 0;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    private ImageButton bSettings;
    private ImageButton bStatistics;
    private ImageButton bPet;
    private ImageButton bInformation;
    private ImageButton bDiary;
    private ImageButton bHeart;
    private ImageButton bSteps;
    private TextView tHeart;
    private TextView tSteps;
    private TextView tPet;
    private Bundle bundle;
    private String heartRate;
    private String steps;
    private Boolean watchConnected = false;

    private SharedPreferences sp;
    private SharedPreferences spHeart;
    private SharedPreferences.Editor editor;

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
        tPet = findViewById(R.id.tPet);
        bInformation = findViewById(R.id.bInfo);
        bDiary = findViewById(R.id.bDiary);
        bHeart = findViewById(R.id.bHeart);
        tSteps = findViewById(R.id.tSteps);
        bSteps = findViewById(R.id.bSteps);

        bHeart.setOnClickListener(v -> {
            handleCameraPermission();
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

        readPetMode();
    }

    /**
     * This method is used to open the camera activity when trying to access the camera for the heart rate measurement.
     */
    private void openCameraActivity(){
        Intent intent = new Intent(getApplicationContext(), HeartRateMonitor.class);
        startActivity(intent);
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

        readPetMode();
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
            }else if(message.charAt(0) =='H'){
                logging(message);
            }
        }
        // simple method to add the log TextView.
        public void logging(String message) {
            String heart = message.substring(1);
            editor = spHeart.edit();
            editor.putString("rate",heart);
            editor.commit();
            tHeart.setText(spHeart.getString("rate", "N/A"));
        }
        public void loggingSteps(String message){
            String steps = message.substring(1);
            editor = spHeart.edit();
            editor.putString("step",steps);
            editor.commit();
            tSteps.setText(spHeart.getString("step", "N/A"));
        }
    }

    /**
     * This method handles if the pet mode is activated or not.
     */
    private void readPetMode(){
        SharedPreferences sp = this.getApplicationContext().getSharedPreferences("settings", 0);

        boolean petModeEnabled = sp.getBoolean("petMode", false);

        if(!petModeEnabled){
            tPet.setBackground(getResources().getDrawable(R.drawable.mainbottom_background_grey));
            bPet.setBackground(getResources().getDrawable(R.drawable.maintop_background_grey));
            bPet.setAlpha(120);
            bPet.setEnabled(false);
        } else {
            tPet.setBackground(getResources().getDrawable(R.drawable.mainbottom_background));
            bPet.setBackground(getResources().getDrawable(R.drawable.maintop_background));
            bPet.setAlpha(255);
            bPet.setEnabled(true);
        }
    }

    /**
     * This method handles the permission for the camera app of the device.
     */
    private void handleCameraPermission(){
        int permissionCheck = checkSelfPermission(Manifest.permission.CAMERA);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CALLBACK_NUMBER);
        } else {
            openCameraActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCameraActivity();
            }
        }
    }
}
