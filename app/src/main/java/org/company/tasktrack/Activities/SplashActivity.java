package org.company.tasktrack.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.company.tasktrack.Application.Config;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.NotificationUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView logolayout = (ImageView) findViewById(R.id.logo);
        logolayout.animate().alpha(1.0f).scaleX(0.9f).scaleY(0.9f).setDuration(2500);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(DbHandler.getBoolean(getApplicationContext(),"isLoggedIn",false))
                    {
                        UserInfoResponse userInfoResponse=new Gson().fromJson(DbHandler.getString(getApplicationContext(),"UserInfo",""),UserInfoResponse.class);
                        if (userInfoResponse.getData().getEmpType().equals("Admin"))
                            intentWithFinish(AdminActivity.class);
                        else if (userInfoResponse.getData().getEmpType().equals("Manager"))
                            intentWithFinish(ManagerActivity.class);
                        else if (userInfoResponse.getData().getEmpType().equals("Employee"))
                            intentWithFinish(EmployeeActivity.class);
                    }
                    else{
                        intentWithFinish(LoginActivity.class);
                    }
                }
            }, 3000);




    }
}
