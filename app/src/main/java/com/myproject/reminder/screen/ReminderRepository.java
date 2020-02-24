package com.myproject.reminder.screen;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.myproject.reminder.retrofit.NetworkAPI;
import com.myproject.reminder.retrofit.output.DietPlanData;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ReminderRepository {
    private NetworkAPI networkAPI;
    private MutableLiveData<DietPlanData> dietPlanDataMutableLiveData;

    @Inject
    ReminderRepository(NetworkAPI networkAPI) {
        this.networkAPI = networkAPI;
    }

    @SuppressLint("CheckResult")
    LiveData<DietPlanData> getDietPlan() {
        dietPlanDataMutableLiveData = new MutableLiveData<>();
        Single<Response<DietPlanData>> responseSingle =
                networkAPI.dietPlan();
        responseSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<DietPlanData>>() {
                    @Override
                    public void onSuccess(Response<DietPlanData> respResponse) {
                        try {
                            dietPlanDataMutableLiveData.postValue(respResponse.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dietPlanDataMutableLiveData.postValue(null);
                    }
                });
        return dietPlanDataMutableLiveData;
    }
}
