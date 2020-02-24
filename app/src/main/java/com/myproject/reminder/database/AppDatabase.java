package com.myproject.reminder.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Diet.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DietDao dietDao();
}
