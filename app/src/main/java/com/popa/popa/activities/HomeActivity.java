package com.popa.popa.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import com.popa.popa.R;

public class HomeActivity extends AppCompatActivity {


    ImageButton bSettings;
    ImageButton bStatistics;
    ImageButton bPet;
    ImageButton bInformation;
    ImageButton bDiary;

    Bundle bundle;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = this.getApplicationContext().getSharedPreferences("user", 0);

        getIntentBundle(getIntent());

        if(bundle == null){
            editor = sp.edit();
            Bundle bundleIfNull = new Bundle();
            bundleIfNull.putString("gender", sp.getString("gender", ""));
            bundleIfNull.putString("age", sp.getString("age", ""));

            this.bundle = bundleIfNull;
        } else {
            editor = sp.edit();
            editor.putString("gender", bundle.getString("gender"));
            editor.putString("age", bundle.getString("age"));
            editor.commit();
        }

        bSettings = findViewById(R.id.bSettings);
        bStatistics = findViewById(R.id.bStatistics);
        bPet = findViewById(R.id.bPet);
        bInformation = findViewById(R.id.bInfo);
        bDiary = findViewById(R.id.bDiary);

        bSettings.setOnClickListener(v -> {
            Intent a = new Intent(v.getContext(), SettingsActivity.class);
            a.putExtras(bundle);
            startActivity(a);
        });

        bStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), StatisticsActivity.class);
            startActivity(intent);
        });

        bPet.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PetActivity.class);
            startActivity(intent);
        });

        bInformation.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InformationActivity.class);
            startActivity(intent);
        });

        bDiary.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DiaryActivity.class);
            startActivity(intent);
        });
    }

    private void getIntentBundle(Intent intent){
        bundle = intent.getExtras();
    }
}
