package com.popa.popa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.popa.popa.R;

public class HomeActivity extends AppCompatActivity {

    Intent intent = null;

//    ImageButton bStatistics = (ImageButton) findViewById(R.id.bStatistics);
//    ImageButton bDiary = (ImageButton) findViewById(R.id.bDiary);
//    ImageButton bInfo = (ImageButton) findViewById(R.id.bInfo);
    ImageButton bSettings;
//    ImageButton bPet = (ImageButton) findViewById(R.id.bPet);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bSettings = (ImageButton) findViewById(R.id.bSettings);

        bSettings.setOnClickListener(v -> {
            intent = new Intent(v.getContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //maybe destroy sensor connection too..?
    }
}
