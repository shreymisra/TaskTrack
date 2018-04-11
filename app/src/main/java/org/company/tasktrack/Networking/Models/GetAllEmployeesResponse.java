package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 11/4/18.
 */

public class GetAllEmployeesResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("employees")
    @Expose
    private List<GetAllEmployeesDatum> employees = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GetAllEmployeesDatum> getEmployees() {
        return employees;
    }

    public void setEmployees(List<GetAllEmployeesDatum> employees) {
        this.employees = employees;
    }
}
