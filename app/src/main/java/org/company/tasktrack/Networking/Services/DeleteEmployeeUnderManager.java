package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.DeleteEmployeeUnderManagerModel;
import org.company.tasktrack.Networking.Models.DeleteEmployeeUnderManagerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 13/4/18.
 */

public interface DeleteEmployeeUnderManager {
    @POST("rem_employee_manager")
    Call<DeleteEmployeeUnderManagerResponse> responseDelete(@Body DeleteEmployeeUnderManagerModel object);
}
