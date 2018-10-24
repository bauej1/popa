package com.popa.popa.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

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
    @NonNull
    private float y;
    @NonNull
    private float z;
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

    @NonNull
    public float getY() {
        return y;
    }

    public void setY(@NonNull float y) {
        this.y = y;
    }

    @NonNull
    public float getZ() {
        return z;
    }

    public void setZ(@NonNull float z) {
        this.z = z;
    }

    public boolean isPosture() {
        return posture;
    }

    public void setPosture(boolean posture) {
        this.posture = posture;
    }
}
