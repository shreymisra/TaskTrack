package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 14/4/18.
 */

public class GetAssignedTaskModel {

    @SerializedName("fdate")
    @Expose
    private String fdate;
    @SerializedName("tdate")
    @Expose
    private String tdate;
    @SerializedName("emp_id")
    @Expose
    private String empId;

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}
