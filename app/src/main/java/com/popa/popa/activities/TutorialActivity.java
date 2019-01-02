package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.popa.popa.R;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    private Button bNextHint;
    private ImageView ivSpeech;
    private ArrayList<ImageView> hints;

    /**
     * Counts which hint is currently shown for the user
     */
    private static int bubbleCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        init();
        initHints();
    }

    /**
     * Initializes all controls in this activity and creates ClickListeners.
     */
    private void init(){
        this.bNextHint = findViewById(R.id.bNextHint);
        this.ivSpeech = findViewById(R.id.ivSpeech);

        bNextHint.setOnClickListener(event -> {
            showNextHint();
        });
    }

    /**
     * Initializes all speech bubbles.
     */
    private void initHints(){
        hints = new ArrayList<>();

        ImageView imageView;

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble);
        hints.add(imageView);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble2);
        hints.add(imageView);
    }

    /**
     * Shows the next hint. It gets the current speech bubble from the drawables folder and sets
     * the image in the view. The image is changed with a fade in animation.
     */
    private void showNextHint(){

        if(bubbleCounter >= hints.size()){
            return;
        }

        ImageView i = hints.get(bubbleCounter);
        ivSpeech.setImageDrawable(i.getDrawable());

        ivSpeech.setVisibility(View.VISIBLE);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        ivSpeech.setAnimation(aniFade);

        ++bubbleCounter;
    }
}
