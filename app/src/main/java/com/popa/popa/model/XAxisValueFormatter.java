package com.popa.popa.model;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XAxisValueFormatter implements IAxisValueFormatter {

    private Date[] mValues;

    public XAxisValueFormatter(Date[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM");
        String strDate = dateFormat.format(mValues[(int) value]);
        return strDate;
    }
}