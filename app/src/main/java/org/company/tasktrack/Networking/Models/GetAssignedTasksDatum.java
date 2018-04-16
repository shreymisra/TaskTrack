package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 14/4/18.
 */

public class GetAssignedTasksDatum {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("remark_manager")
    @Expose
    private String remarkManager;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("task_status")
    @Expose
    private String taskStatus;
    @SerializedName("assign_date")
    @Expose
    private String assignDate;
    @SerializedName("remark_employee")
    @Expose
    private String remark_employee;
    @SerializedName("complete_date")
    @Expose
    private String complete_date;
    @SerializedName("emp_details")
    @Expose
    private List<GetAssignedTasksEmp> empDetails = null;


    public String getRemark_employee() {
        return remark_employee;
    }

    public void setRemark_employee(String remark_employee) {
        this.remark_employee = remark_employee;
    }

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemarkManager() {
        return remarkManager;
    }

    public void setRemarkManager(String remarkManager) {
        this.remarkManager = remarkManager;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public List<GetAssignedTasksEmp> getEmpDetails() {
        return empDetails;
    }

    public void setEmpDetails(List<GetAssignedTasksEmp> empDetails) {
        this.empDetails = empDetails;
    }
}
