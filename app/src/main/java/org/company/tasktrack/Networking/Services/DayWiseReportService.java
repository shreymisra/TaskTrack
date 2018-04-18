package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.DayWiseReportModel;
import org.company.tasktrack.Networking.Models.DayWiseReportReponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 18/4/18.
 */

public interface DayWiseReportService {
    @POST("day_wise_report")
    Call<DayWiseReportReponse> report(@Body DayWiseReportModel object);
}
