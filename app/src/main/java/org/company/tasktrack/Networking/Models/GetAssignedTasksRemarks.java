package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 19/4/18.
 */

public class GetAssignedTasksRemarks {
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
