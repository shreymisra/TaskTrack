package org.company.tasktrack.Networking.Api;

import org.company.tasktrack.Application.Config;
import org.company.tasktrack.Networking.Models.LoginRequestModal;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.Networking.Models.ValidateResponse;
import org.company.tasktrack.Networking.Services.LoginService;
import org.company.tasktrack.Networking.Services.UserInfo;
import org.company.tasktrack.Networking.TaskTrackApi;
import org.company.tasktrack.Utils.DbHandler;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shrey on 6/4/18.
 */

public class AccountApiManager {



    private static AccountApiManager sInstance;

    public static AccountApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new AccountApiManager();
        }
        return sInstance;
    }

    public Observable<ValidateResponse> validate(LoginRequestModal object) {

        return TaskTrackApi.getRetrofitLogin().create(LoginService.class).validate(object)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(s -> {
                    DbHandler.put(Config.DB_SESSION,s);
                    DbHandler.setSession();
                    return Observable.just(s);
                });
    }

    public Observable<UserInfoResponse> userInfo()
    {
        return TaskTrackApi.getRetrofit().create(UserInfo.class).infoResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
