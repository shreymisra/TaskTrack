package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 17/4/18.
 */

public class CheckAttendanceStatusResponse {
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("count")
    @Expose
    public Integer count;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
