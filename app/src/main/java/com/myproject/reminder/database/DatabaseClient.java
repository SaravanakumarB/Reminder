package com.myproject.reminder.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.myproject.reminder.util.Constant;


public class DatabaseClient {

    @SuppressLint("StaticFieldLeak")
    private static DatabaseClient mInstance;
    private Context mCtx;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, Constant.DATABASE_NAME).build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
