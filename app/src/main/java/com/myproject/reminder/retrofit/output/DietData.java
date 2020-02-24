package com.myproject.reminder.retrofit.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DietData {
    @SerializedName("food")
    @Expose
    private String food;
    @SerializedName("meal_time")
    @Expose
    private String mealTime;

    public String getFood() {
        return food;
    }

    public String getMealTime() {
        return mealTime;
    }
}
