package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popa.popa.R;

public class TodayMoodActivity extends AppCompatActivity {

    TextView textMood;
    SeekBar seekBarMood;
    ImageView imageMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_moodtoday);

        textMood = (TextView) findViewById(R.id.textMood);
        seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
        imageMood = (ImageView) findViewById(R.id.imageMood);

        seekBarMood.setMax(10);
        seekBarMood.setProgress(10);

        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMood.setText(String.valueOf(progress));
                int id;

                if (progress <= 1){
                    id = getResources().getIdentifier("crying", "drawable", getPackageName());
                    imageMood.setImageResource(id);
                } else if (progress > 1 && progress <= 3) {
                    id = getResources().getIdentifier("verysad", "drawable", getPackageName());
                    imageMood.setImageResource(id);
                } else if (progress > 3 && progress <=5) {
                    id = getResources().getIdentifier("sad", "drawable", getPackageName());
                    imageMood.setImageResource(id);
                } else if (progress >5 &&  progress <= 8){
                    id = getResources().getIdentifier("smiling", "drawable", getPackageName());
                    imageMood.setImageResource(id);
                } else if (progress > 8 && progress <=10){
                    id = getResources().getIdentifier("happy", "drawable", getPackageName());
                    imageMood.setImageResource(id);
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
