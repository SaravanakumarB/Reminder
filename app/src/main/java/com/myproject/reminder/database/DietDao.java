package com.myproject.reminder.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DietDao {
    @Query("Select * from Diet")
    List<Diet> getDietPlan();

    @Insert
    void insertDiet(Diet diet);

    @Query("Select * from Diet WHERE day == :d ")
    List<Diet> getCurrentDayDietList(int d);
}
