package com.myproject.reminder.backgroundwork;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorkManager extends Worker {
    private static final String WORK_RESULT = "work_result";
    private static final String TAG = "ReminderWorkManager";

    public ReminderWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.e(TAG, "Version Oreo and above");
            Intent mIntent = new Intent(getApplicationContext(), ReminderIntentService.class);
            ReminderIntentService.enqueueWork(getApplicationContext(), mIntent);
        } else {
            Log.e(TAG, "Version below oreo");
            Intent mIntent = new Intent(getApplicationContext(), ReminderIntentService.class);
            getApplicationContext().startService(mIntent);
        }


        Log.d(TAG, "work manager called");

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();

        return Result.success(outputData);

    }

}
