package com.popa.popa.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "dataId",
        onDelete = ForeignKey.CASCADE))
public class PostureData {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int dataId;
    @NonNull
    private String timestamp;
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
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
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
}
