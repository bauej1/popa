package com.popa.popa.activities;

import android.content.Intent;
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
    private Button bSkip;
    private ImageView ivSpeech;
    private ArrayList<ImageView> hints;

    Bundle bundle;
    Intent intent;

    /**
     * Counts which hint is currently shown for the user
     */
    private static int bubbleCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Intent intentBefore = getIntent();
        bundle = intentBefore.getExtras();

        init();
        initHints();
        showNextHint();
    }

    /**
     * Initializes all controls in this activity and creates ClickListeners.
     */
    private void init(){
        this.bNextHint = findViewById(R.id.bNextHint);
        this.ivSpeech = findViewById(R.id.ivSpeech);
        this.bSkip = findViewById(R.id.bSkip);

        bNextHint.setOnClickListener(event -> {
            showNextHint();
        });

        bSkip.setOnClickListener(event ->{
            moveToHome(bundle);
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

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble3);
        hints.add(imageView);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble4);
        hints.add(imageView);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble5);
        hints.add(imageView);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble6);
        hints.add(imageView);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.speechbubble7);
        hints.add(imageView);
    }

    /**
     * Shows the next hint. It gets the current speech bubble from the drawables folder and sets
     * the image in the view. The image is changed with a fade in animation.
     */
    private void showNextHint(){

        if(bubbleCounter >= hints.size()-1){
            moveToHome(bundle);
            bubbleCounter = 0;
            finish();
        }

        ImageView i = hints.get(bubbleCounter);
        ivSpeech.setImageDrawable(i.getDrawable());

        ivSpeech.setVisibility(View.VISIBLE);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        ivSpeech.setAnimation(aniFade);

        ++bubbleCounter;
    }

    private void moveToHome(Bundle bundle){
        Intent intent = new Intent(this, HomeActivity.class);

        if(bundle != null){
            intent.putExtras(bundle);
        }

        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        bubbleCounter = 0;
        super.onBackPressed();
        finish();
    }
}
