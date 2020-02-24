package com.myproject.reminder.backgroundwork;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.work.WorkManager;

import com.myproject.reminder.R;
import com.myproject.reminder.database.DatabaseClient;
import com.myproject.reminder.database.Diet;
import com.myproject.reminder.screen.MainActivity;
import com.myproject.reminder.screen.detail.ReminderActivity;
import com.myproject.reminder.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReminderIntentService extends JobIntentService {
    private static final String TAG = "ReminderIntentService";
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1;
    private static Context mContext;

    public ReminderIntentService() {
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ReminderIntentService.class);
        ReminderIntentService.enqueueWork(context, starter);
    }

    public static void enqueueWork(Context context, Intent intent) {
        mContext = context;
        enqueueWork(context, ReminderIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "Handle work called");
        checkIfDbIsEmpty();
    }

    /*Method to check whether data is already in db*/
    private void checkIfDbIsEmpty() {
        DatabaseClient db = DatabaseClient.getInstance(mContext);
        //here we have given status -1 to get all data
        List<Diet> dietList = db.getAppDatabase().dietDao().getDietPlan();
        if (dietList != null && dietList.size() > 0) {
            Log.e("Sync", "data is there to sync from local");
            checkForTheTodayReminder();
        } else {
            //terminating the work if data is not there
            Intent intent = new Intent(this, ReminderIntentService.class);
            stopService(intent);
            WorkManager.getInstance().cancelAllWorkByTag(Constant.WORK_MANAGER_TAG);
        }
    }

    /*Method to get current day diet plan and to show reminder*/
    private void checkForTheTodayReminder() {
        int currentDay = getCurrentDayOfTheWeek();
        Log.d(TAG, "Current day = " + String.valueOf(currentDay));
        DatabaseClient db = DatabaseClient.getInstance(mContext);
        //get data for the current day in db
        List<Diet> currentDayPlans = db.getAppDatabase().dietDao().getCurrentDayDietList(currentDay);
        //if current day plan is empty, no action needed.
        if (currentDayPlans != null && currentDayPlans.size() > 0) {
            Calendar currentTime = Calendar.getInstance();
            Log.e(TAG, "Current day plan - " + currentDayPlans.size());
            for(Diet diet : currentDayPlans){
                Calendar dietTime = getDietTime(diet.getMealTime());
                if(currentTime.before(dietTime)) {
                    //pass current time as second object
                    if (isDifferenceWithInOneHour(dietTime.getTimeInMillis(), currentTime.getTimeInMillis())) {
                        sendNotification(diet.getFood(),diet.getMealTime(),diet.getFood() + " - " + diet.getMealTime());
                    }
                }
            }
        }
    }

    /*Method to get current day*/
    private int getCurrentDayOfTheWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if (Constant.MONDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 1;
        } else if (Constant.TUESDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 2;
        } else if (Constant.WEDNESDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 3;
        } else if (Constant.THURSDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 4;
        } else if (Constant.FRIDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 5;
        } else if (Constant.SATURDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 6;
        } else if (Constant.SUNDAY.equalsIgnoreCase(dayOfTheWeek)) {
            return 7;
        }

        return 0;
    }

    /*Method to get diet time as calender instance*/
    private Calendar getDietTime(String time) {
        String hour = time.substring(0, time.indexOf(":"));
        String min = time.substring(time.indexOf(":") + 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    /*Method to find difference between two time*/
    private boolean isDifferenceWithInOneHour(long time1, long time2) {
        long difference = time1 - time2;
        int days = (int) (difference / (1000*60*60*24));
        int  hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        hours = (hours < 0 ? -hours : hours);
        if(hours >= 1) {
            return false;
        } else if (hours == 0) {
            if(min <= 5) {
                return true;
            }
        }
        return false;
    }

    private void sendNotification(String food, String mealsTime, String body) {
        Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
        intent.putExtra(Constant.FOOD, food);
        intent.putExtra(Constant.MEALS_TIME, mealsTime);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Reminder Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Upcoming Plan" + " : " + body)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);


        notificationManager.notify(1, notificationBuilder.build());
    }
}
