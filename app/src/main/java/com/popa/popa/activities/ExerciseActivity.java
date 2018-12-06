package com.popa.popa.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.popa.popa.R;

public class ExerciseActivity extends AppCompatActivity {

    Button exercise1;
    Button exercise2;
    Button exercise3;
    Button exercise4;
    Button exercise5;
    Button exercise6;
    Button exercise7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_exercises);

        exercise1 = findViewById(R.id.bEx1);
        exercise2 = findViewById(R.id.bEx2);
        exercise3 = findViewById(R.id.bEx3);
        exercise4 = findViewById(R.id.bEx4);
        exercise5 = findViewById(R.id.bEx5);
        exercise6 = findViewById(R.id.bEx6);
        exercise7 = findViewById(R.id.bEx7);

        exercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View childposeView = inflater.inflate(R.layout.e_childpose,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(childposeView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View forwardfoldView = inflater.inflate(R.layout.e_forwardfold,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(forwardfoldView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View catcowView = inflater.inflate(R.layout.e_catcow,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(catcowView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercise4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View standingcatcowView = inflater.inflate(R.layout.e_standingcatcow,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(standingcatcowView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercise5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View highplankView = inflater.inflate(R.layout.e_highplank,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(highplankView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercise6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View sideplankView = inflater.inflate(R.layout.e_sideplank,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(sideplankView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        exercise7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dogView = inflater.inflate(R.layout.e_downfacingdog,null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
                builder.setView(dogView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}
