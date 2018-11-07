package com.popa.popa.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import com.popa.popa.R;
import com.popa.popa.database.popaDatabase;

public class HomeActivity extends AppCompatActivity {

    Intent intent = null;
    ImageButton bSettings;
    ImageButton bStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bSettings = (ImageButton) findViewById(R.id.bSettings);
        bStatistics = (ImageButton) findViewById(R.id.bStatistics);

        bSettings.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        bStatistics.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), StatisticsActivity.class);
            startActivity(intent);
        });

    }
}
