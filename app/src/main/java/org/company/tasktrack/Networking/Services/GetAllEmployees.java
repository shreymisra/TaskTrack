package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrey on 11/4/18.
 */

public interface GetAllEmployees {

    @GET("employees")
    Call<GetAllEmployeesResponse> getAllEmployees();
}
