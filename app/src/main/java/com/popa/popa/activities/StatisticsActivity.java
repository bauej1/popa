package com.popa.popa.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.popa.popa.R;
import com.popa.popa.database.popaDatabase;
import com.popa.popa.model.PostureData;
import com.popa.popa.model.XAxisValueFormatter;
import com.popa.popa.model.YAxisValueFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsActivity extends AppCompatActivity {

    private View postureChartLayout;
    private View loadingOverlay;
    private BarChart barChartTime;
    private XAxis xAxis;
    private YAxis yAxis;
    private ArrayList<PostureData> postureData;
    private ArrayList<BarEntry> barEntries;
    private BarDataSet barDataSet;
    private BarData barData;
    private popaDatabase database;
    private Date tempDate;
    private Legend legend;
    private ImageButton bReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChartTime = findViewById(R.id.barchart_posture_time);

        bReload = findViewById(R.id.bReload);
        bReload.setOnClickListener(v -> {
            loadData();
        });

        xAxis = barChartTime.getXAxis();
        yAxis = barChartTime.getAxisLeft();
        postureData = new ArrayList<>();
        barEntries = new ArrayList<>();
        database = popaDatabase.getDatabase(this);

        loadData();
    }

    /**
     * This method synchronizes the sensor data from the Room Persistance Library on sync button click.
     */
    private void syncData(){
        loadData();
    }

    /**
     * Loads the posture data from the Room Persistance Library.
     */
    private void loadData(){
        new Thread(() -> {

            loadingSpinner(200, View.VISIBLE);
            postureData = (ArrayList<PostureData>) database.daoAccess().getPostureDataAfterTime();
            buildBarChart();
            loadingSpinner(200, View.GONE);

            //checkIfTableShouldBeCleaned();
        }).start();
    }

    /**
     * This method creates the stacked bar chart to see the percentage of good and bad posture per day with all its attributes and styling issues.
     */
    private void buildBarChart(){

        Date[] lastSevenDays = new Date[7];                 //Last seven days for X-Axis

        int percentages[] = new int[101];                   //Percentages for Y-Axis
        for(int i = 0; i <= 100; i++){                      //Fill percentages 0-100%
            percentages[i] = i;
        }

        for(int i = 0; i < 7; i++){                         //Create a stacked barchart for every day in a week
            barEntries.add(new BarEntry(i, getStackedBarEntryByWeekday(i)));
            lastSevenDays[i] = tempDate;
        }

        barDataSet = new BarDataSet(barEntries, "Percentage of posture per day in one week");
        barDataSet.setDrawIcons(false);
        barDataSet.setColors(new int[]{R.color.badPosture, R.color.goodPosture}, this);
        barDataSet.setStackLabels(new String[]{"Good", "Bad"});
        barData = new BarData(barDataSet);

        barChartTime.setData(barData);                      //Set data in chart
        barChartTime.getAxisRight().setDrawLabels(false);   //remove axis lines
        barChartTime.getAxisRight().setDrawGridLines(false);//remove axis lines
        barChartTime.setFitBars(true);                      //bars should fit the axis and fill the whole graphics
        barChartTime.getDescription().setEnabled(false);    //description in bottom right corner is disabled

        xAxis.setAxisMaximum(6f);                           //X-Axis
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisValueFormatter(lastSevenDays));

        yAxis.setAxisMaximum(100f);                         //Y-Axis
        yAxis.setAxisMinimum(0f);
        yAxis.setGranularity(1f);
        yAxis.setDrawGridLines(false);
        yAxis.setValueFormatter(new YAxisValueFormatter(percentages));

        legend = barChartTime.getLegend();                  //Legend
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setCustom(createLegendEntries());

        barChartTime.invalidate();                          //Draw chart
    }

    /**
     * This method creates a stacked bar for every day in a week.
     *
     * @param weekday - the weekday as number
     * @return  float[] - A float array with two values (ex.: float[20,80] for 20% and 80%) who defines the stacked bar
     */
    private float[] getStackedBarEntryByWeekday(int weekday){

        int from = 7 - weekday;
        int to = 7 - (weekday + 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date fromDate = getDateInWeek(from, false);
        Date toDate;

        if(weekday == 6){
            toDate = getDateInWeek(0, true);
        } else{
            toDate = getDateInWeek(to, false);
        }

        return getPostureByPercentage(fromDate, toDate);
    }

    /**
     * This method calculates the percentage of good and bad posture per day.
     *
     * @param fromDate - The timestamp when the day begins
     * @param toDate - The timestamp when the day ends
     * @return float[] - A float array with two values (ex.: float[20,80] for 20% and 80%) who defines the stacked bar
     */
    private float[] getPostureByPercentage(Date fromDate, Date toDate){

        float[] postureByPercentage = new float[2];
        int numberOfBadPosture = 0;
        ArrayList<PostureData> tempList = postureData.stream()      //filter all sensor data if the data is created during the day asked
                                                    .filter(pd -> pd.getTimestamp().after(fromDate) && pd.getTimestamp().before(toDate))
                                                    .collect(Collectors.toCollection(ArrayList::new));
        int max = tempList.size();
        tempDate = toDate;

        for(PostureData pd : tempList){
            if(pd.isPosture() == false){
                numberOfBadPosture++;
            }
        }

        if(max > 0){
            float percentageBadPosture =  ((float) numberOfBadPosture / max) * 100f;
            postureByPercentage[0] = percentageBadPosture;
            postureByPercentage[1] = 100 - percentageBadPosture;
        } else {
            postureByPercentage[0] = 0f;
            postureByPercentage[1] = 0f;
        }
        Log.d("POPATEST", "fromDate -->" + fromDate.toString() + " | toDate --> " + toDate.toString());
        Log.d("POPATEST", "max -->" + max + "");
        Log.d("POPATEST", "0 -->" + postureByPercentage[0] + "");
        Log.d("POPATEST", "1 -->" + postureByPercentage[1] + "");
        Log.d("POPATEST", "__________________________________________________________");

        return postureByPercentage;
    }

    /**
     * This method creates the entries for the legend in the stacked bar chart
     * @return List of Legend Entries
     */
    private List<LegendEntry> createLegendEntries(){

        List<LegendEntry> entries = new ArrayList<>();
        LegendEntry e1 = new LegendEntry();

        e1.formColor = Color.parseColor("#008080");
        e1.label = "Good";
        LegendEntry e2 = new LegendEntry();
        e2.formColor = Color.parseColor("#CC0000");
        e2.label = "Bad";

        entries.add(e1);
        entries.add(e2);

        return entries;
    }

    /**
     * Creates a loading spinner as long as the charts need to get the data from the Room Library.
     * @param duration - duration of the animation
     * @param show - Visible = 0, Invisible = 4, Gone = 8
     */
    private void loadingSpinner(int duration, int show){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideCharts(show);
                loadingOverlay.bringToFront();
                loadingOverlay.setVisibility(show);
                loadingOverlay.animate().setDuration(duration);
            }
        });
    }

    /**
     * Hides the other charts on the Statistic View during the loading process.
     * @param show - Visible = 0, Invisible = 4, Gone = 8
     */
    private void hideCharts(int show){

        loadingOverlay = findViewById(R.id.progress_overlay);
        postureChartLayout = findViewById(R.id.postureChartLayout);

        if(show == 0){
            postureChartLayout.setVisibility(View.INVISIBLE);
        } else {
            postureChartLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Returns a Date Object on 00:00:00h.
     * @param subtract - number of how many days back in the week the calendar should go.
     * @return date - the date of the day in the week minus the subtract
     */
    private Date getDateInWeek(int subtract, boolean lastDay){
        Calendar cal = Calendar.getInstance();

        if (lastDay){ return cal.getTime(); }

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, -subtract);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Checks if the amount of data in the sqlite database is over 100K.
     */
    private void checkIfTableShouldBeCleaned(){
        if(database.daoAccess().countPostureData() > 100000){
            cleanTable();
        }
    }

    /**
     * Delete the content of the sqlite database table
     */
    private void cleanTable(){
        database.daoAccess().deletePostureData();
    }
}
