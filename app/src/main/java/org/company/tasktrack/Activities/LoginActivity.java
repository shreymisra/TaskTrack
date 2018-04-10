package org.company.tasktrack.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.ButterKnife;

import org.company.tasktrack.Networking.Models.LoginRequestModal;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.Networking.Models.ValidateResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.LoginService;
import org.company.tasktrack.Networking.Services.UserInfo;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.ColoredSnackbar;
import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.NetworkCheck;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    Gson gson;

    @BindView(R.id.inputUserName)
    EditText username;
    @BindView(R.id.inputPassword)
    EditText password;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    ProgressDialog progressDialog;
    ColoredSnackbar coloredSnackbar;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        gson = new Gson();

        progressDialog = new ProgressDialog(this);

        if (getIntentExtras().getBoolean("isLoggedOut", false)) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You have been logged out", Snackbar.LENGTH_INDEFINITE);
            coloredSnackbar.confirm(snackbar).show();
        }
        if (getIntentExtras().getBoolean("isForcedLoggedOut", false)) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Your Session has Expired", Snackbar.LENGTH_INDEFINITE);
            coloredSnackbar.alert(snackbar).show();
        }


    }

    @OnClick(R.id.buttonLogin)
    public void login() {
        if (NetworkCheck.isNetworkAvailable(getApplicationContext())) {
            LoginRequestModal object = new LoginRequestModal();
            object.setEmail(username.getText().toString());
            object.setPassword(password.getText().toString());
            progressDialog.setMessage("Validating User ...");
            progressDialog.setTitle("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            LoginService loginService = ServiceGenerator.createService(LoginService.class, "", "");
            Call<ValidateResponse> call = loginService.validate(object);
            call.enqueue(new Callback<ValidateResponse>() {
                @Override
                public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                    ValidateResponse validateResponse = response.body();
                    if (response.code() == 200) {
                        if (validateResponse.getSuccess()) {
                            DbHandler.putString(getApplicationContext(), "bearer", validateResponse.getToken());
                            UserInfo userInfo = ServiceGenerator.createService(UserInfo.class, validateResponse.getToken());
                            Call<UserInfoResponse> responseCall = userInfo.infoResponse();
                            responseCall.enqueue(new Callback<UserInfoResponse>() {
                                @Override
                                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                                    UserInfoResponse userInfoResponse = response.body();

                                    if (response.code() == 200) {
                                        if (userInfoResponse.getSuccess()) {
                                            DbHandler.setSession(getApplicationContext(), gson.toJson(userInfoResponse));
                                            progressDialog.dismiss();
                                            if (userInfoResponse.getData().getEmpType().equals("Admin"))
                                                intentWithFinish(AdminActivity.class);
                                            else if (userInfoResponse.getData().getEmpType().equals("Manager"))
                                                intentWithFinish(ManagerActivity.class);
                                            else if (userInfoResponse.getData().getEmpType().equals("Employee"))
                                                intentWithFinish(EmployeeActivity.class);
                                        } else {
                                            progressDialog.dismiss();
                                            DbHandler.unsetSession(getApplicationContext(), "isForcedLoggedOut");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    handleNetworkErrors(t, -1);
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            buttonLogin.setEnabled(true);
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Login Failed")
                                    .setMessage(response.message())
                                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            login();
                                        }
                                    })
                                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alert.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ValidateResponse> call, Throwable t) {
                    handleNetworkErrors(t, 1);
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Network Available", Snackbar.LENGTH_LONG);
            coloredSnackbar.alert(snackbar).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Press again to exit.", Snackbar.LENGTH_SHORT);
        coloredSnackbar.warning(snackbar).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
        /*disposables.add(AccountApiManager.getInstance().validate(object)
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

        );*/
