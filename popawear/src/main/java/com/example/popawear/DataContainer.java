package com.example.popawear;

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

    public int getHeartValue() {
        return heartValue;
    }

    public int getMoodValue() {
        return moodValue;
    }

    public int getPainValue() {
        return painValue;
    }

    public int getSleepValue() {
        return sleepValue;
    }

    public int getStepsValue() {
        return stepsValue;
    }

    public void setHeartValue(int heartValue) {
        this.heartValue = heartValue;
    }

    public void setMoodValue(int moodValue) {
        this.moodValue = moodValue;
    }

    public void setPainValue(int painValue) {
        this.painValue = painValue;
    }

    public void setSleepValue(int sleepValue) {
        this.sleepValue = sleepValue;
    }

    public void setStepsValue(int stepsValue) {
        this.stepsValue = stepsValue;
    }
}
