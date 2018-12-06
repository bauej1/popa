package com.popa.popa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popa.popa.R;
import com.popa.popa.model.GestureHandler;

public class TodayMoodActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    TextView textMood;
    SeekBar seekBarMood;
    ImageView imageMood;
    GestureDetector gDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_moodtoday);

        textMood = (TextView) findViewById(R.id.textMood);
        seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
        imageMood = (ImageView) findViewById(R.id.imageMood);
        gDetector = new GestureDetector(this);

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

                Intent intBefore = getIntent();
                Bundle bundle = intBefore.getExtras();
                Intent moodIntent = new Intent(TodayMoodActivity.this, HomeActivity.class);
                bundle.putString("mood", String.valueOf(progress));
                moodIntent.putExtras(bundle);
                startActivity(moodIntent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent firstMotion, MotionEvent secondMotion, float v, float v1) {
        float firstX = firstMotion.getX();
        float secondX = secondMotion.getX();

        GestureHandler handler = GestureHandler.getInstance();

        if ((firstX + 200) > secondX) {
            //handler.swipeRight(this, AgeActivity.class);
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, TodayPainActivity.class));
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }
}
