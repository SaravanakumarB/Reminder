package com.myproject.reminder.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private static final String IS_WORK_STARTED = "isWorkStarted";
    private static final String DIET_DURATION = "dietDuration";
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */


    @Inject
    public SessionManager(Context mCtx) {
        sharedPreference = mCtx.getSharedPreferences(mCtx.getPackageName(), MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.apply();
    }

    /**
     * This method is invoked when the work is started successfully
     * as true.
     */
    public void setWorkStarted(boolean b) {
        editor.putBoolean(IS_WORK_STARTED, b);
        editor.commit();
    }

    /**
     * This method is called to check whether work is started or not.
     *
     * @return work status in boolean.
     */
    public boolean isWorkStarted() {
        return sharedPreference.getBoolean(IS_WORK_STARTED, false);
    }


    /**
     * This method is set the diet duration
     */
    public void setDietDuration(int duration) {
        editor.putInt(DIET_DURATION, duration);
        editor.commit();
    }

    /**
     * This method is get the diet duration
     */
    public int getDietDuration() {
        return sharedPreference.getInt(DIET_DURATION, 0);
    }


    /**
     * This method is invoked to clear all user details that are stored in shared preference.
     */
    public void clearAllCredential() {
        editor.clear();
        editor.commit();
    }
}
