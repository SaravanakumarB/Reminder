package com.myproject.reminder.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Diet")
public class Diet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tableId")
    private int tableId;

    /*day -> 1 - Monday
    * 2 - Tuesday
    * 3 - Wednesday
    * 4 - Thursday
    * 5 - Friday
    * 6 - Saturday
    * 7 - Sunday*/
    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "food")
    private String food;

    @ColumnInfo(name = "mealTime")
    private String mealTime;

    @Ignore
    public Diet() {
    }

    public Diet(int tableId, int day, String food, String mealTime) {
        this.tableId = tableId;
        this.day = day;
        this.food = food;
        this.mealTime = mealTime;
    }

    public int getTableId() {
        return tableId;
    }

    public int getDay() {
        return day;
    }

    public String getFood() {
        return food;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }
}
