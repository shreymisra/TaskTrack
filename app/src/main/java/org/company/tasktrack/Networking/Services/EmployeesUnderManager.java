package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.EmployeesUnderManagerModel;
import org.company.tasktrack.Networking.Models.EmployeesUnderManagerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 13/4/18.
 */

public interface EmployeesUnderManager {
    @POST("managers_employees")
    Call<EmployeesUnderManagerResponse> employees(@Body EmployeesUnderManagerModel object);
}
