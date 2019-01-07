package com.popa.popa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.popa.popa.R;
import com.popa.popa.services.SensorService;

public class SettingsActivity extends Activity{

    private FirebaseAuth mAuth;

   private Switch swConnectSensor;
   private Switch swConnectWatch;
   private Switch swPetMode;

   private TextView tStatus;
   private TextView email;
   private TextView tGender;
   private TextView tAge;
   private SensorService sensorService;
   private Button logOut;
   private Button impressum;

   private Intent intent = null;
   private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getIntentBundle(getIntent());
        init();

        readSettings();

        swConnectSensor.setOnCheckedChangeListener((compoundButton, b) -> {

            if(compoundButton.isChecked()){
                sensorService = new SensorService(this);
                tStatus.setText(getResources().getString(R.string.connected));
            } else {
                sensorService.disconnectSensor();
                tStatus.setText(getResources().getString(R.string.not_connected));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                signOut();
                intent = new Intent(view.getContext(), LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        impressum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View impressum = inflater.inflate(R.layout.impressum,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setView(impressum);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        getCurrentUserEmail();
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();

        swConnectSensor = findViewById(R.id.swConnectSensor);
        swPetMode = findViewById(R.id.swActivatePet);
        swConnectWatch = findViewById(R.id.swConnectWatch);
        tStatus = findViewById(R.id.tStatus);
        logOut = findViewById(R.id.logOutButton);
        email = findViewById(R.id.tEmailSettings);
        impressum = findViewById(R.id.impressumButton);
        tGender = findViewById(R.id.tGender);
        tAge = findViewById(R.id.tAge);

        tGender.setText(bundle.getString("gender"));
        tAge.setText(bundle.getString("age"));
    }

    @Override
    protected void onPause(){
        super.onPause();
        storeSettings();
    }

    private void getCurrentUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = user.getEmail();
        email.setText(mail);
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void getIntentBundle(Intent intent){
        bundle = intent.getExtras();
    }

    /**
     * Store settings in shared preferences "settings".
     */
    private void storeSettings(){
        SharedPreferences sp = this.getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("petMode", swPetMode.isChecked());
//        editor.putBoolean("sensor", swConnectSensor.isChecked());
//        editor.putBoolean("watch", swConnectWatch.isChecked());

        editor.commit();
    }

    /**
     * read the Settings from the shared preferences.
     */
    private void readSettings(){
        SharedPreferences sp = this.getApplicationContext().getSharedPreferences("settings", 0);

//        boolean sensorStatus = sp.getBoolean("sensor", false);
        boolean petStatus = sp.getBoolean("petMode", false);
//        boolean watchStatus = sp.getBoolean("watch", false);

//        setSettingOnControl(swConnectSensor, sensorStatus);
        setSettingOnControl(swPetMode, petStatus);
//        setSettingOnControl(swConnectWatch, watchStatus);
    }

    /**
     * Sets the stored setting status from the shared preferences on the control.
     * @param sw - Switch Control
     * @param value - Boolean if checked or not
     */
    private void setSettingOnControl(Switch sw, boolean value){
        if(value){
            sw.setChecked(true);
        } else {
            sw.setChecked(false);
        }
    }
}
