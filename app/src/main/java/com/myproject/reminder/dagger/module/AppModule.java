package com.myproject.reminder.dagger.module;

import android.content.Context;

import com.google.gson.Gson;
import com.myproject.reminder.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Singleton
    @Provides
    Context provideContext(MainApplication application){
        return application;
    }

    @Singleton
    @Provides
    Gson provideGson(){
        return new Gson();
    }
}
