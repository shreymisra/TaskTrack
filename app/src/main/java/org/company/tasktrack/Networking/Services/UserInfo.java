package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.UserInfoResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by shrey on 6/4/18.
 */

public interface UserInfo {

    @GET("memberinfo")
    Call<UserInfoResponse> infoResponse();
}
