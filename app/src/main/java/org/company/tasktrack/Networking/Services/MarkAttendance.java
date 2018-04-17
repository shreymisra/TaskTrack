package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.MarkAttendanceResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shrey on 17/4/18.
 */

public interface MarkAttendance {
    @GET("mark_attendance")
    Call<MarkAttendanceResponse> mark();
}
