package com.popa.popa.model;

// Will listen to step alerts
public interface StepListener {

    public void step(long timeNs);

}