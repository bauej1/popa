package com.popa.popa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

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

        //Replace with lambda expression!
        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //maybe destroy sensor connection too..?
    }
}
