package org.company.tasktrack.Networking.Api;

/**
 * Created by shrey on 6/4/18.
 */

public class ManagerApiManager {
    private static ManagerApiManager sInstance;

    public static ManagerApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ManagerApiManager();
        }
        return sInstance;
    }
}
