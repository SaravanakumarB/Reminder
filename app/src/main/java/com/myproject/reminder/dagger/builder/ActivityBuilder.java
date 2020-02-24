package com.myproject.reminder.dagger.builder;

import com.myproject.reminder.screen.MainActivity;
import com.myproject.reminder.screen.ReminderModule;
import com.myproject.reminder.screen.detail.ReminderActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = ReminderModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract ReminderActivity contributeReminderActivity();
}
