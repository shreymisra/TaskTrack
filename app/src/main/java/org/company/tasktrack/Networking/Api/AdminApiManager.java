package org.company.tasktrack.Networking.Api;

import org.company.tasktrack.Networking.Models.AddUserResponse;
import org.company.tasktrack.Networking.Services.AddUserService;
import org.company.tasktrack.Networking.TaskTrackApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shrey on 6/4/18.
 */

public class AdminApiManager {

    private static AdminApiManager sInstance;

    public static AdminApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new AdminApiManager();
        }
        return sInstance;
    }

    public Observable<AddUserResponse> addEmployee()  {
        return TaskTrackApi.getRetrofit().create(AddUserService.class).addUser()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());

    }

}
