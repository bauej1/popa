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


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_age);

        textAge = (TextView) findViewById(R.id.text_age);
        seekBarAge = (SeekBar) findViewById(R.id.seekBarAge);
        gDetector = new GestureDetector(this);

        seekBarAge.setMax(50);
        seekBarAge.setProgress(25);

        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress + 10;
                textAge.setText(String.valueOf(progress));

                Intent intBefore = getIntent();
                Bundle bundle = intBefore.getExtras();
                Intent ageIntent = new Intent(AgeActivity.this, PainLocationActivity.class);
                bundle.putString("age", String.valueOf(progress));
                ageIntent.putExtras(bundle);
                startActivity(ageIntent);
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
            startActivity(new Intent(this, PainLocationActivity.class));
        } else {
            startActivity(new Intent(this, GenderActivity.class));
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }
}
