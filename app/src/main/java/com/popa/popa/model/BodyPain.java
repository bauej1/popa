package com.popa.popa.model;

import android.widget.ToggleButton;

public class BodyPain {

    private String path;
    private boolean shoulder;
    private boolean lowerBack;
    private boolean sacrum;
    private boolean otherPain;

    public BodyPain(String path, boolean shoulder, boolean lowerBack, boolean sacrum, boolean otherPain){
        this.path = path;
        this.shoulder = shoulder;
        this.lowerBack = lowerBack;
        this.sacrum = sacrum;
        this.otherPain = otherPain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isShoulder() {
        return shoulder;
    }

    public void setShoulder(boolean shoulder) {
        this.shoulder = shoulder;
    }

    public boolean isLowerBack() {
        return lowerBack;
    }

    public void setLowerBack(boolean lowerBack) {
        this.lowerBack = lowerBack;
    }

    public boolean isSacrum() {
        return sacrum;
    }

    public void setSacrum(boolean sacrum) {
        this.sacrum = sacrum;
    }

    public boolean isOtherPain() {
        return otherPain;
    }

    public void setOtherPain(boolean otherPain) {
        this.otherPain = otherPain;
    }
}
