package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.popa.popa.R;

import pl.droidsonroids.gif.GifImageView;

public class GenderActivity extends AppCompatActivity {

    ImageButton buttonGenderFemale;
    ImageButton buttonGenderMale;
    TextView textSwipe;
    GifImageView gifSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_gender);

        buttonGenderFemale = (ImageButton) findViewById(R.id.genderF);
        buttonGenderMale = (ImageButton) findViewById(R.id.genderM);
        textSwipe = (TextView) findViewById(R.id.swipeText);
        gifSwipe = (GifImageView) findViewById(R.id.swipeGif);

        textSwipe.setVisibility(View.INVISIBLE);
        gifSwipe.setVisibility(View.INVISIBLE);

        buttonGenderFemale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                buttonGenderMale.setImageAlpha(63);
                buttonGenderFemale.setImageAlpha(255);
                textSwipe.setVisibility(View.VISIBLE);
                gifSwipe.setVisibility(View.VISIBLE);
            }
        });

        buttonGenderMale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                buttonGenderFemale.setImageAlpha(63);
                buttonGenderMale.setImageAlpha(255);
                textSwipe.setVisibility(View.VISIBLE);
                gifSwipe.setVisibility(View.VISIBLE);
            }
        });
    }
}
