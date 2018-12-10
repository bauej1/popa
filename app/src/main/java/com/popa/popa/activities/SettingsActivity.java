package com.popa.popa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

    Switch swConnect;
    TextView tStatus;
    TextView email;
    SensorService sensorService;
    Button logOut;
    Button impressum;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        swConnect = (Switch) findViewById(R.id.swConnectSensor);
        tStatus = (TextView) findViewById(R.id.tStatus);
        logOut = (Button) findViewById(R.id.logOutButton);
        email = findViewById(R.id.tEmailSettings);
        impressum = findViewById(R.id.impressumButton);

        swConnect.setOnCheckedChangeListener((compoundButton, b) -> {

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

    private void getCurrentUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = user.getEmail();
        email.setText(mail);
    }

    private void signOut() {
        mAuth.signOut();
    }
}
