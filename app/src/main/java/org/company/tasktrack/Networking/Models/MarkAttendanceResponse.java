package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 17/4/18.
 */

public class MarkAttendanceResponse {
    @SerializedName("success")
    @Expose
    public  boolean success;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("msg")
    @Expose
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
