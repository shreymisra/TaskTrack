package org.company.tasktrack.Networking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrey on 18/4/18.
 */

public class DayWiseReportReponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("report")
    @Expose
    private List<DayWiseReportDatum> report = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<DayWiseReportDatum> getReport() {
        return report;
    }

    public void setReport(List<DayWiseReportDatum> report) {
        this.report = report;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
