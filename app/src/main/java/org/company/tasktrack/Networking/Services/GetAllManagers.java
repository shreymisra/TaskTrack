package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.GetAllManagersResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrey on 11/4/18.
 */

public interface GetAllManagers {
    @GET("managers")
    Call<GetAllManagersResponse> getAllManagers();
}
