package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 18/4/18.
 */

public class DayWiseReportDatum {


    @SerializedName("time_start")
    @Expose
    private String timeStart;
    @SerializedName("time_end")
    @Expose
    private String timeEnd;
    @SerializedName("tasks")
    @Expose
    private List<DayWiseReportTasks> tasks = null;
    @SerializedName("red_count")
    @Expose
    private String red_count;

    public String getRed_count() {
        return red_count;
    }

    public void setRed_count(String red_count) {
        this.red_count = red_count;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<DayWiseReportTasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<DayWiseReportTasks> tasks) {
        this.tasks = tasks;
    }
}
