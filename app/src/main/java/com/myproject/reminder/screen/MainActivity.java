package com.myproject.reminder.screen;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.myproject.reminder.R;
import com.myproject.reminder.adapter.DietListAdapter;
import com.myproject.reminder.backgroundwork.ReminderIntentService;
import com.myproject.reminder.backgroundwork.ReminderWorkManager;
import com.myproject.reminder.base.BaseActivity;
import com.myproject.reminder.database.DatabaseClient;
import com.myproject.reminder.database.Diet;
import com.myproject.reminder.databinding.ActivityMainBinding;
import com.myproject.reminder.retrofit.output.DietData;
import com.myproject.reminder.retrofit.output.DietPlanData;
import com.myproject.reminder.retrofit.output.WeekDietData;
import com.myproject.reminder.util.Constant;
import com.myproject.reminder.util.SessionManager;
import com.myproject.reminder.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ReminderViewModel> {
    /* ------------------------------------------------------------- *
     * Constant Members
     * ------------------------------------------------------------- */

    private static final String TAG = "MainActivity";

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private ActivityMainBinding binding;
    private ReminderViewModel viewModel;

    /* ------------------------------------------------------------- *
     * Injected Class
     * ------------------------------------------------------------- */

    @Inject
    SessionManager sessionManager;
    @Inject
    Utils utils;
    @Inject
    ViewModelProvider.Factory factory;

    /* ------------------------------------------------------------- *
     * View Model
     * ------------------------------------------------------------- */

    @Override
    public ReminderViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(ReminderViewModel.class);
        return viewModel;
    }

    /* ------------------------------------------------------------- *
     * Overriding Base Activity Methods
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //set support action bar
        setToolBar(binding.toolbar, getString(R.string.app_name));

        //layout manager
        binding.rvDietPlan.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        //check the db whether data is already there
        checkWhetherDataIsInDb();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /*Method to check whether data is already in db*/

    private void checkWhetherDataIsInDb() {
        new Thread(() -> {
            DatabaseClient db = DatabaseClient.getInstance(MainActivity.this);
            List<Diet> dietList = db.getAppDatabase().dietDao().getDietPlan();
            runOnUiThread(() -> {
                if (dietList != null && dietList.size() > 0) {
                    Log.e(TAG, "data is in db");
                    setValuesToAdapter(dietList);
                    setUpPeriodicTask();
                } else {
                    Log.e(TAG, "No data in db");
                    //get data from server
                    callDietPlanFromApi();
                }
            });

        }).start();
    }

    /*Method to hit the api and get the data*/

    private void callDietPlanFromApi() {
        if(utils.isConnectingToInternet()) {
            utils.showProgressDialog(this);
            viewModel.getDietPlan().observe(this, dietPlanData -> {
                utils.dismissProgressDialog();
                if (dietPlanData != null) {
                    setDataToList(dietPlanData);
                } else {
                    utils.showSnackbar(MainActivity.this, getString(R.string.api_error_text));
                }
            });
        } else {
            utils.showSnackbar(this, getString(R.string.network_unavailable));
        }
    }

    /*Checking whether diet plan is empty*/
    private void setDataToList(DietPlanData dietPlanData) {
        //save diet duration in preference
        if(dietPlanData.getDietDuration() != null) {
            sessionManager.setDietDuration(dietPlanData.getDietDuration());
        }
        //check data and save in db
        if (dietPlanData.getWeekDietData() != null) {
            insertInLocalDb(dietPlanData.getWeekDietData());
        }
    }

    /*Inserting the data into local db*/

    private void insertInLocalDb(WeekDietData weekDietData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Diet> dietList = new ArrayList<>();
                if (weekDietData.getMonday() != null && weekDietData.getMonday().size() > 0) {
                    Log.e(TAG, "Inserting Monday plan to db");
                    //insert it one by one in db
                    for (DietData dietData: weekDietData.getMonday()) {
                        Diet diet = new Diet();
                        diet.setFood(dietData.getFood());
                        diet.setMealTime(dietData.getMealTime());
                        diet.setDay(1);
                        dietList.add(diet);
                        DatabaseClient db = DatabaseClient.getInstance(MainActivity.this);
                        db.getAppDatabase().dietDao().insertDiet(diet);
                    }
                }

                if (weekDietData.getWednesday() != null && weekDietData.getWednesday().size() > 0) {
                    Log.e(TAG, "Inserting Wednesday plan to db");
                    //insert it one by one in db
                    for (DietData dietData: weekDietData.getWednesday()) {
                        Diet diet = new Diet();
                        diet.setFood(dietData.getFood());
                        diet.setMealTime(dietData.getMealTime());
                        diet.setDay(3);
                        dietList.add(diet);
                        DatabaseClient db = DatabaseClient.getInstance(MainActivity.this);
                        db.getAppDatabase().dietDao().insertDiet(diet);
                    }
                }

                if (weekDietData.getThursday() != null && weekDietData.getThursday().size() > 0) {
                    Log.e(TAG, "Inserting Thursday plan to db");
                    //insert it one by one in db
                    for (DietData dietData: weekDietData.getThursday()) {
                        Diet diet = new Diet();
                        diet.setFood(dietData.getFood());
                        diet.setMealTime(dietData.getMealTime());
                        diet.setDay(4);
                        dietList.add(diet);
                        DatabaseClient db = DatabaseClient.getInstance(MainActivity.this);
                        db.getAppDatabase().dietDao().insertDiet(diet);
                    }
                }

                runOnUiThread(() -> {
                    setValuesToAdapter(dietList);
                    setUpPeriodicTask();
                });
            }
        }).start();
    }

    /*Method to set the diet list in recycler view*/

    private void setValuesToAdapter(List<Diet> diets) {
        DietListAdapter adapter = new DietListAdapter(diets);
        binding.rvDietPlan.setAdapter(adapter);
    }

    /*Method to Start the work manager based on constraint*/

    private void setUpPeriodicTask() {
        if(!sessionManager.isWorkStarted()) {
            //set in preference that sync started as true
            Log.d(TAG, "Started");
            sessionManager.setWorkStarted(true);
            //define constraints
            Constraints myConstraints = new Constraints.Builder()
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .setRequiresBatteryNotLow(false)
                    .setRequiresStorageNotLow(false)
                    .build();

            Data source = new Data.Builder()
                    .putString("workType", "PeriodicTime")
                    .build();

            PeriodicWorkRequest refreshCpnWork =
                    new PeriodicWorkRequest.Builder(ReminderWorkManager.class, 5, TimeUnit.MINUTES)
                            .setConstraints(myConstraints)
                            .setInputData(source)
                            .build();

            WorkManager.getInstance().enqueueUniquePeriodicWork(Constant.WORK_MANAGER_TAG,  ExistingPeriodicWorkPolicy.REPLACE, refreshCpnWork);
        }
    }

    /*Method to terminate work and intent service*/

    private void terminateWork() {
        WorkManager.getInstance().cancelAllWorkByTag(Constant.WORK_MANAGER_TAG);
        sessionManager.setWorkStarted(false);
        Intent intent = new Intent(this, ReminderIntentService.class);
        stopService(intent);
    }
}
