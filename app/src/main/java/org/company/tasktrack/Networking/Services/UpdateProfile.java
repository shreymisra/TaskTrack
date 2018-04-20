package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.UpdateProfileModel;
import org.company.tasktrack.Networking.Models.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 21/4/18.
 */

public interface UpdateProfile {
    @POST("update_profile")
    Call<UpdateProfileResponse> responseUpdate(@Body UpdateProfileModel object);
}
