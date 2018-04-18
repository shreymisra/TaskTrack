package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 18/4/18.
 */

public class DayWiseReportTasks {
    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("color")
    @Expose
    private String color;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
