package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.UpdatePasswordModel;
import org.company.tasktrack.Networking.Models.UpdatePasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 20/4/18.
 */

public interface UpdatePassword {
    @POST("update_password")
    Call<UpdatePasswordResponse> responseUpdatePassword(@Body UpdatePasswordModel object);
}
