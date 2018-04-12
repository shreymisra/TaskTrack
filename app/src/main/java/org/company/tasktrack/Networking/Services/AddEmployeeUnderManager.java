package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.AddEmployeesUnderManagerModel;
import org.company.tasktrack.Networking.Models.AddEmployeesUnderManagerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 13/4/18.
 */

public interface AddEmployeeUnderManager {
    @POST("add_managers_employees")
    Call<AddEmployeesUnderManagerResponse> responseAdded(@Body AddEmployeesUnderManagerModel object);
}
