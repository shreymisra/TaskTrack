package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.AddUserModel;
import org.company.tasktrack.Networking.Models.AddUserResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 6/4/18.
 */

public interface AddUserService {
    @POST("signup")
    Observable<AddUserResponse> addUser(@Body AddUserModel object);
}
