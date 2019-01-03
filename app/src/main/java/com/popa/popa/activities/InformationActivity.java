package com.popa.popa.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.popa.popa.R;

public class InformationActivity extends AppCompatActivity {

    Intent intent = null;
    Button posture;
    Button exercises;
    Button tips;
    Button clothes;
    Button tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        posture = findViewById(R.id.bPosture);
        exercises = findViewById(R.id.bExercises);
        tips = findViewById(R.id.bTips);
        clothes = findViewById(R.id.bClothes);
        tutorial = findViewById(R.id.bTutorial);

        posture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View postureView = inflater.inflate(R.layout.i_posture,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                builder.setView(postureView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercises.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), ExerciseActivity.class);
            startActivity(intent);
        });

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View tipsView = inflater.inflate(R.layout.i_tips,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                builder.setView(tipsView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View clothesView = inflater.inflate(R.layout.i_clothes, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                builder.setView(clothesView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        tutorial.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TutorialActivity.class);
            startActivity(intent);
        });


    }
}
