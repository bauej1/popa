package com.popa.popa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popa.popa.R;
import com.popa.popa.model.GestureHandler;


public class AgeActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    TextView textAge;
    SeekBar seekBarAge;
    GestureDetector gDetector;

    Intent intent;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_age);

        Intent intentBefore = getIntent();
        bundle = intentBefore.getExtras();

        textAge = (TextView) findViewById(R.id.text_age);
        seekBarAge = (SeekBar) findViewById(R.id.seekBarAge);
        gDetector = new GestureDetector(this);

        // Sets the maximum value of seek bar to 50
        seekBarAge.setMax(50);

        // Sets actual value of seek bar to 25
        seekBarAge.setProgress(25);

        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Sets the minimum value of seek bar to 10
                progress = progress + 10;

                // Sets the text as value from seek bar
                textAge.setText(String.valueOf(progress));

                // Packs the value from seek bar in bundle
                intent = new Intent(AgeActivity.this, PainLocationActivity.class);
                bundle.putString("age", String.valueOf(progress));
                intent.putExtras(bundle);
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

        if ((firstX + 200) > secondX) {
            // Starts PainLocationActivity if swipe from right to left
            startActivity(intent);
        } else {
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }
}
