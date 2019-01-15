package com.popa.popa.model;

/**
 * Code used from http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.XD30EFxKiUk
 */
// Will listen to step alerts
public interface StepListener {

    public void step(long timeNs);

}