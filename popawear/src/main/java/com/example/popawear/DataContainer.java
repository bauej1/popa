package com.example.popawear;

/**
 * This is a datacointainer build as a Singleton, to save data inside the app.
 */

public class DataContainer {
    private DataContainer(){

    }
    private static DataContainer dataContainer = new DataContainer();

    public static DataContainer getInstance(){
        return dataContainer;
    }

    private int heartValue;
    private int stepsValue = 0;
    private int moodValue = 0;
    private int painValue = 0;
    private int sleepValue = 0;
    //returns the measured and saved heart frequency value
    public int getHeartValue() {
        return heartValue;
    }

    //return mood Value from diary
    public int getMoodValue() {
        return moodValue;
    }

    //returnd pain value from diary
    public int getPainValue() {
        return painValue;
    }

    //return Sleep value from diary
    public int getSleepValue() {
        return sleepValue;
    }

    //return steps value from stepcounter
    public int getStepsValue() {
        return stepsValue;
    }

    //sets the hheartfrequeny value
    public void setHeartValue(int heartValue) {
        this.heartValue = heartValue;
    }

    //sets the mood value
    public void setMoodValue(int moodValue) {
        this.moodValue = moodValue;
    }


    //sets the pain Value
    public void setPainValue(int painValue) {
        this.painValue = painValue;
    }

    //sets the sleep value
    public void setSleepValue(int sleepValue) {
        this.sleepValue = sleepValue;
    }

    //sets the steps value
    public void setStepsValue(int stepsValue) {
        this.stepsValue = stepsValue;
    }
}
