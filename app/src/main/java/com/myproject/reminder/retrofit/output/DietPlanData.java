package com.myproject.reminder.retrofit.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DietPlanData {
    @SerializedName("diet_duration")
    @Expose
    private Integer dietDuration;
    @SerializedName("week_diet_data")
    @Expose
    private WeekDietData weekDietData;

    public Integer getDietDuration() {
        return dietDuration;
    }

    public WeekDietData getWeekDietData() {
        return weekDietData;
    }
}
