package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.FcmIdModel;
import org.company.tasktrack.Networking.Models.FcmIdResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 17/4/18.
 */

public interface SendFcmId {
    @POST("fcm_id")
    Call<FcmIdResponse> sendId(@Body FcmIdModel object);
}
