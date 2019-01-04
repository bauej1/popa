package com.popa.popa.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.wearable.DataItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popa.popa.R;
import com.popa.popa.model.diaryDataItem;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static android.content.ContentValues.TAG;


public class DiaryActivity extends AppCompatActivity {

    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    String datapath = "/message_path";
    FloatingActionButton addDiaryEntry;
    TextView tvDiaryEntry;
    TextView test;

    ArrayList<diaryDataItem> diaryDataList;
    ArrayList<diaryDataItem> moodDataList;
    ArrayList<diaryDataItem> painDataList;
    ArrayList<diaryDataItem> sleepDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);

        addDiaryEntry = findViewById(R.id.addDiaryEntry);
        tvDiaryEntry = findViewById(R.id.tvDiaryEntry);
        test = findViewById(R.id.test);
        List<String> integerData = new ArrayList<String>();

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        loadData();
        loadData2();
        loadData3();
        addDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("moodDataList", null);
        Type type = new TypeToken<ArrayList<diaryDataItem>>() {}.getType();
        moodDataList = gson.fromJson(json, type);

        if(moodDataList == null){
            moodDataList = new ArrayList<>();
        }
        //Log.d("moodAray", "value: "+ moodDataList.get(0).getDate());
        //Log.d("moodAray", "value: "+ moodDataList.get(0).getValue());
        //Log.d("moodAray", "value: "+ moodDataList.get(0).getValueString());



    }
    private void loadData2(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("painDataList", null);
        Type type = new TypeToken<ArrayList<diaryDataItem>>() {}.getType();
        painDataList = gson.fromJson(json, type);

        if(painDataList == null){
            painDataList = new ArrayList<>();
        }
    //    Log.d("moodAray", ""+painDataList.get(0).getDate()+painDataList.get(0).getValue());

    }
    private void loadData3(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("sleepDataList", null);
        Type type = new TypeToken<ArrayList<diaryDataItem>>() {}.getType();
        sleepDataList = gson.fromJson(json, type);

        if(sleepDataList == null){
            sleepDataList = new ArrayList<>();
        }
      //  Log.d("moodAray", ""+sleepDataList.get(0).getDate()+sleepDataList.get(0).getValue());

    }
/////////////////////////////////////////////// Added the Receiver for the Smart Watch ///////////////////////////////////////////////////////
//setup a broadcast receiver to receive the messages from the wear device via the listenerService.
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Log.v(TAG, "Main activity received message: " + message);
        logging(message);
    }
}

    // simple method to add the log TextView.
    public void logging(String message) {
        LocalDateTime date = LocalDateTime.now();
        switch (message.charAt(0)){
            case 'M' :
                    String msg = message.substring(1);
                    moodDataList.add(new diaryDataItem( msg,date ));
                    saveDataMood();
                Log.d("THIS IS MOOD", message);
                Log.d("THIS IS ", message.substring(1));
                break;

            case 'P' :
                msg = message.substring(1);
                painDataList.add(new diaryDataItem( msg, date));
                saveDataPain();
                Log.d("THIS IS PAIN", message);
                break;

            case 'S' :
                msg = message.substring(1);
                sleepDataList.add(new diaryDataItem( msg, date));
                saveDataSleep();
                Log.d("THIS IS SLEEP", message);
                break;
        }
    }

    private void saveDataMood(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(moodDataList);
        editor.putString("moodDataList", json);
        editor.apply();
    }
    private void saveDataPain(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(painDataList);
        editor.putString("painDataList", json);
        editor.apply();
    }
    private void saveDataSleep(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sleepDataList);
        editor.putString("sleepDataList", json);
        editor.apply();
    }
}
