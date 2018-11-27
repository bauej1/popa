package com.example.popawear;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends WearableActivity {
    Intent intent = null;
    private TextView stepCount;
    private TextView heartFrequency;
    private Button button_diary;
    private Button button_heartFrequency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        stepCount = (TextView) findViewById(R.id.text_stepCount);
        heartFrequency = (TextView) findViewById(R.id.text_heart);
        button_diary = findViewById(R.id.button_diary);
        button_heartFrequency =  findViewById(R.id.button_heart);

        button_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        button_heartFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), HeartActivity.class);
                startActivity(intent);
            }
        });


    }
};
