package com.popa.popa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mbientlab.metawear.MetaWearBoard;

public class SettingsActivity extends Activity {

    Switch swConnect;
    TextView tStatus;

    SensorService sensorService;
    MetaWearBoard board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swConnect = (Switch) findViewById(R.id.swConnect);
        tStatus = (TextView) findViewById(R.id.tStatus);

        sensorService = new SensorService(this);
        sensorService.attachService();

        //replace with lambda expression!
        swConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    sensorService.retrieveBoard();
                    tStatus.setText(getResources().getString(R.string.connected));
                } else {
                    sensorService.disconnectSensor();
                    tStatus.setText(getResources().getString(R.string.not_connected));
                }
            }
        });
    }
}
