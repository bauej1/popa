package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popa.popa.R;

public class TodayPainActivity extends AppCompatActivity {

    TextView textPain;
    SeekBar seekBarPain;
    ImageView imageSmile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_paintoday);

        textPain = (TextView) findViewById(R.id.textPain);
        seekBarPain = (SeekBar) findViewById(R.id.seekBarPain);
        imageSmile = (ImageView) findViewById(R.id.imageSmile);

        seekBarPain.setMax(10);
        seekBarPain.setProgress(0);

        seekBarPain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textPain.setText(String.valueOf(progress));
                int id;

                if (progress <= 2){
                    id = getResources().getIdentifier("happy", "drawable", getPackageName());
                    imageSmile.setImageResource(id);
                } else if (progress > 2 && progress <= 4) {
                    id = getResources().getIdentifier("smiling", "drawable", getPackageName());
                    imageSmile.setImageResource(id);
                } else if (progress > 4 && progress <=6) {
                    id = getResources().getIdentifier("sad", "drawable", getPackageName());
                    imageSmile.setImageResource(id);
                } else if (progress >6 &&  progress <= 8){
                    id = getResources().getIdentifier("verysad", "drawable", getPackageName());
                    imageSmile.setImageResource(id);
                } else if (progress > 8 && progress <=10){
                    id = getResources().getIdentifier("crying", "drawable", getPackageName());
                    imageSmile.setImageResource(id);
                }
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
