package com.popa.popa.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
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
    }
}
