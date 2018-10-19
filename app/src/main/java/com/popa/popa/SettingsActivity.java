package com.popa.popa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import com.mbientlab.metawear.module.Led;
import com.mbientlab.metawear.module.Led.Color;

public class SettingsActivity extends Activity {

    Switch swConnect;
    TextView tStatus;
    SensorService sensorService;
    Led led;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swConnect = (Switch) findViewById(R.id.swConnectSensor);
        tStatus = (TextView) findViewById(R.id.tStatus);

        sensorService = new SensorService(this);
        sensorService.attachService();

        swConnect.setOnCheckedChangeListener((compoundButton, b) -> {

            Log.d("checked status: ", compoundButton.isChecked() + "");

            if(compoundButton.isChecked()){
                sensorService.retrieveBoard();



                led = sensorService.getBoard().getModule(Led.class);

                if(led == null){
                    Log.d("led is null", "led is null");
                } else {
                    Log.d("led is not null", "led is not null");
                }

//                led.editPattern(Color.BLUE);
//                led.play();
                tStatus.setText(getResources().getString(R.string.connected));

            } else {
                sensorService.disconnectSensor();
                tStatus.setText(getResources().getString(R.string.not_connected));
            }
        });
    }
}
