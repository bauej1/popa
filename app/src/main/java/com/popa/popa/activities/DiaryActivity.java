package com.popa.popa.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popa.popa.R;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    FloatingActionButton addDiaryEntry;
    TextView tvDiaryEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);

        addDiaryEntry = findViewById(R.id.addDiaryEntry);
        tvDiaryEntry = findViewById(R.id.tvDiaryEntry);
        List<String> integerData = new ArrayList<String>();

        addDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //setContentView(R.layout.diary_entry);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View diaryEntryView = inflater.inflate(R.layout.diary_entry_test,null, false);
                final SeekBar seekBarMood = diaryEntryView.findViewById(R.id.seekyBarMood);
                final SeekBar seekBarPain = diaryEntryView.findViewById(R.id.seekyBarPain);
                final SeekBar seekBarSleep = diaryEntryView.findViewById(R.id.seekyBarSleep);
                final TextView textMood = diaryEntryView.findViewById(R.id.textyMood);
                final TextView textPain = diaryEntryView.findViewById(R.id.textyPain);
                final TextView textSleep = diaryEntryView.findViewById(R.id.textySleep);

                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryActivity.this);
                builder.setView(diaryEntryView);

                seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        textMood.setText(String.valueOf(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                seekBarPain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        textPain.setText(String.valueOf(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                seekBarSleep.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        textSleep.setText(String.valueOf(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                builder.setPositiveButton(R.string.addDiaryEntry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //tvDiaryEntry.setText("Mood: " + seekBarMood.getProgress() + " Pain: " + seekBarPain.getProgress() + " Sleep: " + seekBarMood.getProgress());
                        integerData.add("Sleep: " + seekBarSleep.getProgress() + " Pain: " + seekBarPain.getProgress() + " Mood: " + seekBarMood.getProgress());

                        tvDiaryEntry.setText("");

                        for (int j = 0; j < integerData.size(); j++){
                            tvDiaryEntry.append(integerData.get(j));
                            tvDiaryEntry.append("\n");
                        }

                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#d3d3d3"));
                //new AlertDialog.Builder(DiaryActivity.this).setView(diaryEntryView).setTitle("").show();
            }
        });
    }
}
