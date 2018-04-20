package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 20/4/18.
 */

public class UpdatePasswordModel {
    @SerializedName("user_password")
    @Expose
    private String oldPass;
    @SerializedName("password")
    @Expose
    private String newPass;
    @SerializedName("emp_id")
    @Expose
    private String emp_id;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
