package com.popa.popa.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.popa.popa.R;
import com.popa.popa.services.SensorService;

public class SettingsActivity extends Activity{

    Switch swConnect;
    TextView tStatus;
    SensorService sensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swConnect = (Switch) findViewById(R.id.swConnectSensor);
        tStatus = (TextView) findViewById(R.id.tStatus);

        swConnect.setOnCheckedChangeListener((compoundButton, b) -> {

            if(compoundButton.isChecked()){
                sensorService = new SensorService(this);
                tStatus.setText(getResources().getString(R.string.connected));
            } else {
                sensorService.disconnectSensor();
                tStatus.setText(getResources().getString(R.string.not_connected));
            }
        });
    }
}
