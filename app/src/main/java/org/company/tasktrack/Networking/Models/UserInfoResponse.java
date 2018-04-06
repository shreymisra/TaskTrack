package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 6/4/18.
 */

public class UserInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private UserInfoData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UserInfoData getData() {
        return data;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }
}
