package org.company.tasktrack.Networking.Api;

/**
 * Created by shrey on 6/4/18.
 */

public class EmployeeApiManager {

    private static EmployeeApiManager sInstance;

    public static EmployeeApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new EmployeeApiManager();
        }
        return sInstance;
    }
}
