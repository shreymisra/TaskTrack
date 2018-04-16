package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 14/4/18.
 */

public class GetAssignedTaskResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("tasks")
    @Expose
    private List<GetAssignedTasksDatum> tasks = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GetAssignedTasksDatum> getTasks() {
        return tasks;
    }

    public void setTasks(List<GetAssignedTasksDatum> tasks) {
        this.tasks = tasks;
    }


}

