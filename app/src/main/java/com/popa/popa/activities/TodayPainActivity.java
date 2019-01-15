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

public class TodayPainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private TextView textPain;
    private SeekBar seekBarPain;
    private ImageView imageSmile;
    private GestureDetector gDetector;

    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_paintoday);

        Intent intentBefore = getIntent();
        bundle = intentBefore.getExtras();

        textPain = findViewById(R.id.textPain);
        seekBarPain = findViewById(R.id.seekBarPain);
        imageSmile = findViewById(R.id.imageSmile);
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

                intent = new Intent(TodayPainActivity.this, TodayMoodActivity.class);
                bundle.putString("pain", String.valueOf(progress));
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
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }
}
