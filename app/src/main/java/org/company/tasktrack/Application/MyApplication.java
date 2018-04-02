package org.company.tasktrack.Application;

import android.app.Application;

import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.Utils;

/**
 * Created by anurag on 5/10/17.
 */

public class MyApplication extends Application {
    public static MyApplication context;

    public static MyApplication getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        DbHandler.initialize(this);
        Utils.initialize(this);
    }
}