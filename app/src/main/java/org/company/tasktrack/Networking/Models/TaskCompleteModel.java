package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 15/4/18.
 */

public class TaskCompleteModel {
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("emp_remark")
    @Expose
    private String empRemark;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getEmpRemark() {
        return empRemark;
    }

    public void setEmpRemark(String empRemark) {
        this.empRemark = empRemark;
    }
}
