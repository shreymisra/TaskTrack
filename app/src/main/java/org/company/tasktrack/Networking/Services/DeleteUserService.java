package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.DeleteUserModel;
import org.company.tasktrack.Networking.Models.DeleteUserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 13/4/18.
 */

public interface DeleteUserService {
    @POST("delete_emp")
    Call<DeleteUserResponse> deleteResponse(@Body DeleteUserModel object);
}
