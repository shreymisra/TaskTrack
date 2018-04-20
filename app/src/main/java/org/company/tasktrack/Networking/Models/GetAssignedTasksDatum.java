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
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
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
    @SerializedName("hour_remark")
    @Expose
    private List<GetAssignedTasksRemarks> hourRemark = null;
    @SerializedName("task_status")
    @Expose
    private String taskStatus;
    @SerializedName("assign_date")
    @Expose
    private String assignDate;
    @SerializedName("emp_details")
    @Expose
    private List<GetAssignedTasksEmp> empDetails = null;
    @SerializedName("remark_employee")
    @Expose
    private String remarkEmployee;
    @SerializedName("complete_date")
    @Expose
    private String completeDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public List<GetAssignedTasksRemarks> getHourRemark() {
        return hourRemark;
    }

    public void setHourRemark(List<GetAssignedTasksRemarks> hourRemark) {
        this.hourRemark = hourRemark;
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

    public String getRemarkEmployee() {
        return remarkEmployee;
    }

    public void setRemarkEmployee(String remarkEmployee) {
        this.remarkEmployee = remarkEmployee;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

}
