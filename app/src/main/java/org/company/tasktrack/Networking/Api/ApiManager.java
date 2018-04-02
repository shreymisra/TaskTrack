package org.company.tasktrack.Networking.Api;


import io.reactivex.Observable;

/**
 * Created by shrey on 2/4/18.
 */

public class ApiManager {

    private static ApiManager sInstance;

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }
        return sInstance;
    }


}
