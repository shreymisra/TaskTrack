package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 13/4/18.
 */

public class EmployeesUnderManagerResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("employees")
    @Expose
    private List<EmployeesUnderManagerDatum> employees = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<EmployeesUnderManagerDatum> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeesUnderManagerDatum> employees) {
        this.employees = employees;
    }

}
