package com.myproject.reminder;


import com.myproject.reminder.dagger.component.DaggerReminderMainComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MainApplication extends DaggerApplication {
    private static final String TAG = MainApplication.class.getSimpleName();
    private static MainApplication instance;

    public static synchronized MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerReminderMainComponent.builder().create(this);
    }
}
