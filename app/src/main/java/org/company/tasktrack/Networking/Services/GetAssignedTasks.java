package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.GetAssignedTaskModel;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 14/4/18.
 */

public interface GetAssignedTasks {

    @POST("date_range_report")
    Call<GetAssignedTaskResponse> responseTasks(@Body GetAssignedTaskModel object);
}
