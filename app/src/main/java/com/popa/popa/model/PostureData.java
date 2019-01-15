package com.popa.popa.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "PostureData")
public class PostureData {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int dataId;
    @NonNull
    @TypeConverters(DateConverter.class)
    private Date timestamp;
    @NonNull
    private float x;
    private boolean posture;

    public PostureData(){
    }

    @NonNull
    public int getDataId() {
        return dataId;
    }

    public void setDataId(@NonNull int dataId) {
        this.dataId = dataId;
    }

    @NonNull
    public float getX() {
        return x;
    }

    public void setX(@NonNull float x) {
        this.x = x;
    }

    public boolean isPosture() {
        return posture;
    }

    public void setPosture(boolean posture) {
        this.posture = posture;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }
}

