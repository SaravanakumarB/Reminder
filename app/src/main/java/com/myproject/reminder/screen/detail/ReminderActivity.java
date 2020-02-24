package com.myproject.reminder.screen.detail;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.os.Bundle;

import com.myproject.reminder.R;
import com.myproject.reminder.base.BaseActivity;
import com.myproject.reminder.databinding.ActivityReminderBinding;
import com.myproject.reminder.screen.MainActivity;
import com.myproject.reminder.util.Constant;
import com.myproject.reminder.util.SessionManager;

import javax.inject.Inject;

public class ReminderActivity extends BaseActivity {
    ActivityReminderBinding binding;

    @Inject
    SessionManager sessionManager;

    @Override
    public ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder);

        if(getIntent() != null) {
            String food = getIntent().getStringExtra(Constant.FOOD);
            String time = getIntent().getStringExtra(Constant.MEALS_TIME);

            binding.tvFood.setText(String.format("Food - %s", food));
            binding.tvTime.setText(String.format("Time - %s", time));
            binding.tvDuration.setText(String.format("Duration - %s", sessionManager.getDietDuration()));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
