package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popa.popa.R;


public class AgeActivity extends AppCompatActivity {

    TextView textAge;
    SeekBar seekBarAge;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_age);

        textAge = (TextView) findViewById(R.id.text_age);
        seekBarAge = (SeekBar) findViewById(R.id.seekBarAge);

        seekBarAge.setMax(50);
        seekBarAge.setProgress(25);

        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress + 10;
                textAge.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
