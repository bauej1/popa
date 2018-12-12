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

public class TodayPainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    TextView textPain;
    SeekBar seekBarPain;
    ImageView imageSmile;
    GestureDetector gDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_paintoday);

        textPain = (TextView) findViewById(R.id.textPain);
        seekBarPain = (SeekBar) findViewById(R.id.seekBarPain);
        imageSmile = (ImageView) findViewById(R.id.imageSmile);
        gDetector = new GestureDetector(this);

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

                Intent intBefore = getIntent();
                Bundle bundle = intBefore.getExtras();
                Intent painIntent = new Intent(TodayPainActivity.this, TodayMoodActivity.class);
                bundle.putString("pain", String.valueOf(progress));
                painIntent.putExtras(bundle);
                startActivity(painIntent);
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
            startActivity(new Intent(this, TodayMoodActivity.class));
        } else {
            startActivity(new Intent(this, PainLocationActivity.class));
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }
}
