package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.CheckAttendanceStatusResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrey on 17/4/18.
 */

public interface CheckAttendanceStatus {
    @GET("attendance")
    Call<CheckAttendanceStatusResponse> check();
}
