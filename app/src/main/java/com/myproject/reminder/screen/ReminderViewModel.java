package com.myproject.reminder.screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.myproject.reminder.retrofit.output.DietPlanData;

import javax.inject.Inject;

public class ReminderViewModel extends ViewModel {
    private ReminderRepository repository;

    @Inject
    ReminderViewModel(ReminderRepository repository) {
        this.repository = repository;
    }

    LiveData<DietPlanData> getDietPlan() {
        return repository.getDietPlan();
    }
}
