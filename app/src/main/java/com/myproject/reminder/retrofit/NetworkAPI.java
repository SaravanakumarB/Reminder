package com.myproject.reminder.retrofit;

import com.myproject.reminder.retrofit.output.DietPlanData;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface NetworkAPI {
    @GET("dummy/")
    Single<Response<DietPlanData>> dietPlan();
}
