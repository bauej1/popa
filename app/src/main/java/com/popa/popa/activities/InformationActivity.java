package com.popa.popa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.popa.popa.R;

public class InformationActivity extends AppCompatActivity {

    Button exercises;
    Button tips;
    Button clothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        exercises = findViewById(R.id.bExercises);
        tips = findViewById(R.id.bTips);
        clothes = findViewById(R.id.bClothes);


    }
}
