package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.TaskCompleteModel;
import org.company.tasktrack.Networking.Models.TaskCompleteResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 15/4/18.
 */

public interface TaskComplete {
    @POST("mark_as_complete")
    Call<TaskCompleteResponse> response(@Body TaskCompleteModel object);
}
