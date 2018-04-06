package org.company.tasktrack.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import org.company.tasktrack.Networking.Api.AccountApiManager;
import org.company.tasktrack.Networking.Api.EmployeeApiManager;
import org.company.tasktrack.Networking.Models.LoginRequestModal;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.inputUserName)
    EditText username;
    @BindView(R.id.inputPassword)
    EditText password;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        /*if(DbHandler.isSessionSet())
        {
            UserInfoResponse response=DbHandler.get("UserInfo",UserInfoResponse.class);
            if(response.getData().getEmpType().equals("Employee"))
                intentWithFinish(EmployeeApiManager.class);
            else if(response.getData().getEmpType().equals("Manager"))
                intentWithFinish(ManagerActivity.class);
            else if(response.getData().getEmpType().equals("Admin"))
                intentWithFinish(AdminActivity.class);
        }*/
        progressDialog=new ProgressDialog(this);
    }

    @OnClick(R.id.buttonLogin)
    public void login()
    {
        LoginRequestModal object=new LoginRequestModal();
        object.setEmail(username.getText().toString());
        object.setPassword(password.getText().toString());
        progressDialog.setMessage("Validating User..");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        disposables.add(AccountApiManager.getInstance().validate(object)
                    .subscribe(validateResponse -> {
                        if(validateResponse.getSuccess()) {
                            DbHandler.put("bearer", validateResponse.getToken());

                            disposables.add(AccountApiManager.getInstance().userInfo()
                                    .subscribe(userInfoResponse -> {
                                        progressDialog.dismiss();
                                        DbHandler.put("UserInfo", userInfoResponse);
                                        if (userInfoResponse.getData().getEmpType().equals("Admin"))
                                            intentWithFinish(AdminActivity.class);
                                        else if (userInfoResponse.getData().getEmpType().equals("Manager"))
                                            intentWithFinish(ManagerActivity.class);
                                        else if (userInfoResponse.getData().getEmpType().equals("Employee"))
                                            intentWithFinish(EmployeeActivity.class);
                                    }, e -> {
                                        handleNetworkErrors(e, -1);
                                        progressDialog.dismiss();
                                    })
                            );
                        }else{
                            new AlertDialog.Builder(this)
                                    .setTitle("Login Failed")
                                    .setMessage("Please verify your credentials.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                        },e->{handleNetworkErrors(e,1);progressDialog.dismiss();})

        );
    }
}
