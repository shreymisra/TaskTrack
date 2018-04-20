package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 21/4/18.
 */

public class UpdateProfileModel {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("emp_id")
    @Expose
    private String emp_id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
