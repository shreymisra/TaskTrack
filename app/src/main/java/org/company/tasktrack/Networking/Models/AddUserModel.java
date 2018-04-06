package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrey on 7/4/18.
 */

public class AddUserModel {
    @SerializedName("name")
    @Expose
    private  String name;

    @SerializedName("password")
    @Expose
    private  String password;

    @SerializedName("email")
    @Expose
    private  String email;

    @SerializedName("mob")
    @Expose
    private  String mobile;

    @SerializedName("type")
    @Expose
    private  String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
