package com.popa.popa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import com.popa.popa.R;

public class HomeActivity extends AppCompatActivity {

    Intent intent = null;
    ImageButton bSettings;
    ImageButton bStatistics;
    ImageButton bPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bSettings = findViewById(R.id.bSettings);
        bStatistics = findViewById(R.id.bStatistics);
        bPet = findViewById(R.id.bPet);

        bSettings.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        bStatistics.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), StatisticsActivity.class);
            startActivity(intent);
        });

        bPet.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), PetActivity.class);
            startActivity(intent);
        });
    }
}
