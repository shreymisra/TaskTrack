package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.LoginRequestModal;
import org.company.tasktrack.Networking.Models.ValidateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 5/4/18.
 */

public interface LoginService  {
    @POST("authenticate")
    Call<ValidateResponse> validate(@Body LoginRequestModal object);
}
