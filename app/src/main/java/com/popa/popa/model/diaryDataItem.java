package com.popa.popa.model;

import java.time.LocalDateTime;

public class diaryDataItem {
    private LocalDateTime date;
    private String value;
    public diaryDataItem(String value, LocalDateTime date){
        this.value = value;
        this.date = date;
    }
    public int getValue(){
        return Integer.parseInt(this.value);
    }

    public LocalDateTime getDate(){
        return date;
    }

    public String getValueString(){
        return this.value;
    }
}
