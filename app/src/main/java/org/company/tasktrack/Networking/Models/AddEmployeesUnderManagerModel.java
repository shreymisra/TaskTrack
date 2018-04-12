package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 13/4/18.
 */

public class AddEmployeesUnderManagerModel {

    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("manager_id")
    @Expose
    private String managerId;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
