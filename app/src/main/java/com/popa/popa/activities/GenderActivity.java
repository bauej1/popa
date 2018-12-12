package com.popa.popa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.popa.popa.R;
import com.popa.popa.model.GestureHandler;

import pl.droidsonroids.gif.GifImageView;

public class GenderActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    ImageButton buttonGenderFemale;
    ImageButton buttonGenderMale;
    TextView textSwipe;
    GifImageView gifSwipe;
    GestureDetector gDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_gender);

        buttonGenderFemale = (ImageButton) findViewById(R.id.genderF);
        buttonGenderMale = (ImageButton) findViewById(R.id.genderM);
        textSwipe = (TextView) findViewById(R.id.swipeText);
        gifSwipe = (GifImageView) findViewById(R.id.swipeGif);
        gDetector = new GestureDetector(this);

        textSwipe.setVisibility(View.INVISIBLE);
        gifSwipe.setVisibility(View.INVISIBLE);

        buttonGenderFemale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                buttonGenderMale.setImageAlpha(63);
                buttonGenderFemale.setImageAlpha(255);
                textSwipe.setVisibility(View.VISIBLE);
                gifSwipe.setVisibility(View.VISIBLE);
                Intent genderIntent = new Intent(GenderActivity.this, AgeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("gender", "female");
                genderIntent.putExtras(bundle);
                startActivity(genderIntent);
            }
        });

        buttonGenderMale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                buttonGenderFemale.setImageAlpha(63);
                buttonGenderMale.setImageAlpha(255);
                textSwipe.setVisibility(View.VISIBLE);
                gifSwipe.setVisibility(View.VISIBLE);
                Intent genderIntent = new Intent(GenderActivity.this, AgeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("gender", "male");
                genderIntent.putExtras(bundle);
                startActivity(genderIntent);
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
            startActivity(new Intent(this, AgeActivity.class));
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }
}
