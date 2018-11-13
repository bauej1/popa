package com.popa.popa.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.popa.popa.R;
import com.popa.popa.services.SensorService;

public class SettingsActivity extends Activity{

    private FirebaseAuth mAuth;

    Switch swConnect;
    TextView tStatus;
    SensorService sensorService;
    Button logOut;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        swConnect = (Switch) findViewById(R.id.swConnectSensor);
        tStatus = (TextView) findViewById(R.id.tStatus);
        logOut = (Button) findViewById(R.id.logOutButton);

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


    }

    private void signOut() {
        mAuth.signOut();
    }
}
