package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 13/4/18.
 */

public class EmployeesUnderManagerDatum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("emp_id")
    @Expose
    private Integer empId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }


}
