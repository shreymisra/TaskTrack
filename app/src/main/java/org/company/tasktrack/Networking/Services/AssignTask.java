package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.AssignTaskModel;
import org.company.tasktrack.Networking.Models.AssignTaskResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 13/4/18.
 */

public interface AssignTask {
    @POST("assign_task")
    Call<AssignTaskResponse> response(@Body AssignTaskModel object);
}
