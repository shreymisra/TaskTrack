package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.DeleteTaskModel;
import org.company.tasktrack.Networking.Models.DeleteTaskResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 14/4/18.
 */

public interface DeleteTask {
    @POST("delete_task")
    Call<DeleteTaskResponse> responseDelete(@Body DeleteTaskModel object);
}
