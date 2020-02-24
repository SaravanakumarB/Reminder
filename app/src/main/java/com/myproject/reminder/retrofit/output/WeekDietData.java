package com.myproject.reminder.retrofit.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeekDietData {
    @SerializedName("thursday")
    @Expose
    private List<DietData> thursday = null;
    @SerializedName("wednesday")
    @Expose
    private List<DietData> wednesday = null;
    @SerializedName("monday")
    @Expose
    private List<DietData> monday = null;


    public List<DietData> getThursday() {
        return thursday;
    }

    public List<DietData> getWednesday() {
        return wednesday;
    }

    public List<DietData> getMonday() {
        return monday;
    }
}
