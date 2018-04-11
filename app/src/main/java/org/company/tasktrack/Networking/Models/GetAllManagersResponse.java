package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 11/4/18.
 */

public class GetAllManagersResponse {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("managers")
    @Expose
    private List<GetAllManagersDatum> managers = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GetAllManagersDatum> getManagers() {
        return managers;
    }

    public void setManagers(List<GetAllManagersDatum> managers) {
        this.managers = managers;
    }

}
