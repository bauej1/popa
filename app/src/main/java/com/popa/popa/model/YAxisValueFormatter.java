package com.popa.popa.model;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class YAxisValueFormatter implements IAxisValueFormatter {

    private int[] mValues;

    public YAxisValueFormatter(int[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value] + "%";
    }
}