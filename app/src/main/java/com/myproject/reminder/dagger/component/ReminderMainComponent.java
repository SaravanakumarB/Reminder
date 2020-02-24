package com.myproject.reminder.dagger.component;


import com.myproject.reminder.MainApplication;
import com.myproject.reminder.dagger.builder.ActivityBuilder;
import com.myproject.reminder.dagger.module.AppModule;
import com.myproject.reminder.retrofit.NetworkService;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class,
        NetworkService.class,
        ActivityBuilder.class})

public interface ReminderMainComponent extends AndroidInjector<MainApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MainApplication> {

    }

}
