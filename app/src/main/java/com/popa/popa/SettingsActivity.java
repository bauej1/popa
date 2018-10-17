package com.popa.popa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    Switch swConnect;
    TextView tStatus;

    SensorService sensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swConnect = (Switch) findViewById(R.id.swConnect);
        tStatus = (TextView) findViewById(R.id.tStatus);

        sensorService = new SensorService(this);
        sensorService.attachService();

        swConnect.setOnCheckedChangeListener((compoundButton, b) -> {
                if(compoundButton.isChecked()){
                    sensorService.retrieveBoard();
                    tStatus.setText(getResources().getString(R.string.connected));
                } else {
                    sensorService.disconnectSensor();
                    tStatus.setText(getResources().getString(R.string.not_connected));
                }
        });
    }
}
