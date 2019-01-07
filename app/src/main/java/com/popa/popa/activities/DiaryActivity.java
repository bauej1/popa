package com.popa.popa.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popa.popa.R;
import com.popa.popa.model.diaryDataItem;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DiaryActivity extends AppCompatActivity {

    FloatingActionButton addDiaryEntry;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    ArrayList<diaryDataItem> moodDataList;
    ArrayList<diaryDataItem> painDataList;
    ArrayList<diaryDataItem> sleepDataList;

    //Chart-Variables
    private BarChart barchart_pain;
    private BarChart barchart_sleep;
    private BarChart barchart_mood;
    private XAxis xAxis_pain, xAxis_sleep, xAxis_mood;
    private YAxis yAxis_pain, yAxis_sleep, yAxis_mood;
    private ArrayList<BarEntry> barEntries_pain;
    private ArrayList<BarEntry> barEntries_sleep;
    private ArrayList<BarEntry> barEntries_mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);

        addDiaryEntry = findViewById(R.id.addDiaryEntry);

        if(isDiaryDone()){
            addDiaryEntry.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));
            addDiaryEntry.setEnabled(false);
        } else {
            addDiaryEntry.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.tileBackground)));
            addDiaryEntry.setEnabled(true);
        }

        initChartVariables();

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        loadData();
        loadData2();
        loadData3();
        sp = this.getApplicationContext().getSharedPreferences("pet", 0);
        addDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isDiaryDone()){
                    return;
                }

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
                        editor = sp.edit();
                        int diaryInputs = Integer.valueOf(sp.getString("diaryInput","0"));
                        diaryInputs = diaryInputs+1;
                        editor.putString("diaryInput",""+diaryInputs);
                        editor.commit();
                        moodDataList.add(new diaryDataItem(seekBarMood.getProgress() + "", LocalDateTime.now()));
                        painDataList.add(new diaryDataItem(seekBarPain.getProgress() + "", LocalDateTime.now()));
                        sleepDataList.add(new diaryDataItem(seekBarSleep.getProgress() + "", LocalDateTime.now()));

                        setDiaryStatus();
                        addDiaryEntry.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));
                        prepareDataForChart();

                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#d3d3d3"));
            }
        });
        prepareDataForChart();
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

    @Override
    protected void onPause(){
        super.onPause();
        saveDataMood();
        saveDataPain();
        saveDataSleep();
    }

    /**
     * Initializes the variables which are useful for building the chart.
     */
    private void initChartVariables(){
        barchart_pain = findViewById(R.id.barchart_pain);
        barchart_mood = findViewById(R.id.barchart_mood);
        barchart_sleep = findViewById(R.id.barchart_sleep);
        xAxis_pain = barchart_pain.getXAxis();
        yAxis_pain = barchart_pain.getAxisLeft();
        xAxis_sleep = barchart_sleep.getXAxis();
        yAxis_sleep = barchart_sleep.getAxisLeft();
        xAxis_mood = barchart_mood.getXAxis();
        yAxis_mood = barchart_mood.getAxisLeft();
        barEntries_pain = new ArrayList<>();
        barEntries_mood = new ArrayList<>();
        barEntries_sleep = new ArrayList<>();
    }

    /**
     * This method concats the data from the 3 specific arraylists (pain, sleep, mood) and make one arraylist out of it.
     */
    private void prepareDataForChart(){
        for(int i = 0; i < moodDataList.size(); ++i){
            barEntries_pain.add(new BarEntry(i, painDataList.get(i).getValue()));
            barEntries_sleep.add(new BarEntry(i, sleepDataList.get(i).getValue()));
            barEntries_mood.add(new BarEntry(i, moodDataList.get(i).getValue()));
        }

        //Then build the bar chart
        buildChart();
    }

    /**
     * This method builds the bar chart with its axis and legends including the data.
     */
    private void buildChart(){

        BarDataSet set_pain = new BarDataSet(barEntries_pain, "Pain");
        set_pain.setColors(new int[]{getResources().getColor(R.color.tileBackground)});

        BarDataSet set_sleep = new BarDataSet(barEntries_sleep, "Sleep");
        set_sleep.setColors(new int[]{getResources().getColor(R.color.tileBackground)});

        BarDataSet set_mood = new BarDataSet(barEntries_mood, "Mood");
        set_mood.setColors(new int[]{getResources().getColor(R.color.tileBackground)});

        BarData data_pain = new BarData(set_pain);
        BarData data_sleep = new BarData(set_sleep);
        BarData data_mood = new BarData(set_mood);

        data_pain.setBarWidth(0.7f);
        data_sleep.setBarWidth(0.7f);
        data_mood.setBarWidth(0.7f);

        barchart_pain.setData(data_pain);
        barchart_sleep.setData(data_sleep);
        barchart_mood.setData(data_mood);

        configChartStyle(barchart_pain);
        configChartStyle(barchart_sleep);
        configChartStyle(barchart_mood);

        configAxis(xAxis_pain, yAxis_pain);
        configAxis(xAxis_sleep, yAxis_sleep);
        configAxis(xAxis_mood, yAxis_mood);

        barchart_pain.invalidate();
        barchart_sleep.invalidate();
        barchart_mood.invalidate();
    }

    /**
     * Configurates the X and Y Axis of the charts
     * @param xAxis - the XAxis
     * @param yAxis - the YAxis
     */
    private void configAxis(XAxis xAxis, YAxis yAxis){
        xAxis.setAxisMaximum(6f);                           //X-Axis
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        yAxis.setAxisMaximum(10f);                         //Y-Axis
        yAxis.setAxisMinimum(0f);
        yAxis.setGranularity(1f);
        yAxis.setDrawGridLines(false);
    }

    private void configChartStyle(BarChart chart){
        chart.setFitBars(true);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);      //remove axis lines
        chart.getAxisRight().setDrawGridLines(false);   //remove axis lines
    }

    /**
     * Sets the status of the diary (filled out or not) in the shared preferences of the android device.
     */
    private void setDiaryStatus(){
        SharedPreferences sp = this.getApplicationContext().getSharedPreferences("diary", 0);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("timestamp", LocalDateTime.now() + "");
        editor.commit();
    }

    /**
     * Checks if the diary is already filled out for the todays day.
     * @return true if diary is already filled out.
     */
    private boolean isDiaryDone(){
        SharedPreferences sp = this.getApplicationContext().getSharedPreferences("diary", 0);
        String timestamp = sp.getString("timestamp", "0");

        if(timestamp.equals("0")) return false;

        String storedDay = timestamp.substring(7,9);
        String nowDay = LocalDateTime.now().toString().substring(7,9);

        if(storedDay.equals(nowDay)){
            return true;
        } else {
            return false;
        }
    }
}
