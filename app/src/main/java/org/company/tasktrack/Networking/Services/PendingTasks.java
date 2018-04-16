package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrey on 15/4/18.
 */

public interface PendingTasks {
    @GET("pending_tasks")
    Call<GetAssignedTaskResponse> responseTasks();
}
