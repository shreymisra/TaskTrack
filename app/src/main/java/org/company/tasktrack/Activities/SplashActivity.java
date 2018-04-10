package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
}
