package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.RequestTaskResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrey on 18/4/18.
 */

public interface RequestTaskService {
    @GET("request_task")
    Call<RequestTaskResponse> response();
}
