package org.company.tasktrack.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesActivity;
import com.google.gson.Gson;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import org.company.tasktrack.Networking.Models.FcmLogoutResponse;
import org.company.tasktrack.Networking.Models.UpdatePasswordModel;
import org.company.tasktrack.Networking.Models.UpdatePasswordResponse;
import org.company.tasktrack.Networking.Models.UpdateProfileModel;
import org.company.tasktrack.Networking.Models.UpdateProfileResponse;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.FcmLogoutService;
import org.company.tasktrack.Networking.Services.UpdatePassword;
import org.company.tasktrack.Networking.Services.UpdateProfile;
import org.company.tasktrack.Networking.Services.UserInfo;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shrey on 10/4/18.
 */

public class AccountActivity extends BaseActivity {

    @BindView(R.id.role)
    TextView role;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.employeeImage)
    ImageView employeeImage;
    @BindView(R.id.name)
    TextView name;

    ProgressDialog progressDialog;
    Gson gson;
    UserInfoResponse userInfoResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        gson=new Gson();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("User Account");
        userInfoResponse=new UserInfoResponse();
        progressDialog=new ProgressDialog(this);
        if(DbHandler.contains(getApplicationContext(),"UserInfo")){
             userInfoResponse=gson.fromJson(DbHandler.getString(getApplicationContext(),"UserInfo",""), UserInfoResponse.class);
            role.setText(userInfoResponse.getData().getEmpType());
            email.setText(userInfoResponse.getData().getEmail());
            name.setText(userInfoResponse.getData().getName()+" ("+userInfoResponse.getData().getEmpId()+")");
        }
        else{
            UserInfo userInfo = ServiceGenerator.createService(UserInfo.class, DbHandler.getString(getApplicationContext(),"bearer",""));
            Call<UserInfoResponse> responseCall = userInfo.infoResponse();
            responseCall.enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    userInfoResponse = response.body();

                    if (response.code() == 200) {
                        if (userInfoResponse.getSuccess()) {
                            DbHandler.putString(getApplicationContext(),"UserInfo",gson.toJson(userInfoResponse));
                            role.setText(userInfoResponse.getData().getEmpType());
                            email.setText(userInfoResponse.getData().getEmail());
                            name.setText(userInfoResponse.getData().getName()+" ("+userInfoResponse.getData().getEmpId()+")");
                        } else{
                            Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(response.code()==403){
                        DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                    handleNetworkErrors(t, -1);
                }
            });
        }

    }

    @OnClick(R.id.options_rate)
    void onRate() {
        final String my_package_name = getPackageName();
        String url = "";
        try {
            getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "market://details?id=" + my_package_name;
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=" + my_package_name;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    @OnClick(R.id.options_feedback)
    void onFeedback() {
        String mailto = "mailto:shrey.1513097@kiet.edu?subject=" + Uri.encode("APP Feedback");
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install an E-Mail App", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.options_logout)
    void onLogout() {
        FcmLogoutService logoutService=ServiceGenerator.createService(FcmLogoutService.class,DbHandler.getString(getApplicationContext(),"bearer",""));
        Call<FcmLogoutResponse> logoutResponseCall=logoutService.response();
        logoutResponseCall.enqueue(new Callback<FcmLogoutResponse>() {
            @Override
            public void onResponse(Call<FcmLogoutResponse> call, Response<FcmLogoutResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getApplicationContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                }else{
                    DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<FcmLogoutResponse> call, Throwable t) {
                handleNetworkErrors(t,-1);
            }
        });

        logout();

    }


    @OnClick(R.id.options_licences)
    void onLicences() {
        OssLicensesMenuActivity.setActivityTitle("Open source licenses");
        intentWithoutFinish(OssLicensesMenuActivity.class);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit:
                editProfile();
                //intentWithoutFinish(NotificationsActivity.class);
                break;
            case R.id.changePassword:
                changePassword();
                //intentWithoutFinish(AccountActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void changePassword(){

        View dialog= LayoutInflater.from(this).inflate(R.layout.change_password,null);
        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(dialog)
                .setMessage(null)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UpdatePasswordModel object=new UpdatePasswordModel();
                        EditText current=(EditText)dialog.findViewById(R.id.current);
                        EditText newPass=(EditText)dialog.findViewById(R.id.newPass);
                        EditText confrimPass=(EditText)dialog.findViewById(R.id.confirmPass);

                        if(newPass.getText().toString().length()!=0&&current.getText().toString().length()!=0) {
                            if (newPass.equals(confrimPass)) {
                                Toast.makeText(getApplicationContext(), "New Password and Confirmed Password must be same .", Toast.LENGTH_SHORT).show();
                            } else {

                                progressDialog.setTitle("Please Wait");
                                progressDialog.setMessage("Changing  your password ...");
                                progressDialog.show();

                                object.setNewPass(newPass.getText().toString());
                                object.setOldPass(current.getText().toString());
                                object.setEmp_id(String.valueOf(userInfoResponse.getData().getEmpId()));

                                UpdatePassword updatePassword = ServiceGenerator.createService(UpdatePassword.class, DbHandler.getString(getApplicationContext(), "bearer", ""));
                                Call<UpdatePasswordResponse> call = updatePassword.responseUpdatePassword(object);
                                call.enqueue(new Callback<UpdatePasswordResponse>() {
                                    @Override
                                    public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                                        progressDialog.dismiss();
                                        UpdatePasswordResponse passwordResponse=response.body();
                                        if(response.code()==200){
                                            Toast.makeText(getApplicationContext(),passwordResponse.getMsg(), Toast.LENGTH_LONG).show();
                                            dialogInterface.dismiss();
                                        }
                                        else if(response.code()==403){
                                            Toast.makeText(getApplicationContext(),passwordResponse.getMsg(), Toast.LENGTH_LONG).show();
                                            DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                                        progressDialog.dismiss();
                                        handleNetworkErrors(t,1);
                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Password fields cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.ic_vpn_key_black_24dp))
                .create().show();

    }
    public void editProfile(){

        View dialog= LayoutInflater.from(this).inflate(R.layout.profile_update,null);
        new AlertDialog.Builder(this)
                .setTitle("Update Details")
                .setView(dialog)
                .setMessage(null)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText email2=(EditText)dialog.findViewById(R.id.email);
                        EditText phone=(EditText)dialog.findViewById(R.id.phone);

                        if(email2.getText().toString().length()==0||phone.getText().toString().length()==0){
                            Toast.makeText(getApplicationContext(),"Phone Number and Email cannot be left blank",Toast.LENGTH_SHORT).show();
                        }else{
                            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email2.getText().toString()).matches()){
                                if(!Pattern.matches("[a-zA-Z]+", phone.getText().toString())&&phone.getText().toString().length()==10){
                                    progressDialog.setTitle("Please Wait");
                                    progressDialog.setMessage("Updating your details ...");
                                    progressDialog.show();

                                    UpdateProfileModel object=new UpdateProfileModel();
                                    object.setEmail(email2.getText().toString());
                                    object.setMob(phone.getText().toString());
                                    object.setEmp_id(String.valueOf(userInfoResponse.getData().getEmpId()));

                                    UpdateProfile profile=ServiceGenerator.createService(UpdateProfile.class,DbHandler.getString(getApplicationContext(),"bearer",""));
                                    Call<UpdateProfileResponse> call=profile.responseUpdate(object);
                                    call.enqueue(new Callback<UpdateProfileResponse>() {
                                        @Override
                                        public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                                            progressDialog.dismiss();
                                            UpdateProfileResponse profileResponse=response.body();
                                            if(response.code()==200){
                                                if(profileResponse.isSuccess())
                                                    email.setText(email2.getText().toString());
                                                Toast.makeText(getApplicationContext(),profileResponse.getMsg(),Toast.LENGTH_LONG).show();
                                                dialogInterface.dismiss();
                                            }else if(response.code()==403){
                                                dialogInterface.dismiss();
                                                DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                                            progressDialog.dismiss();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Enter valid Phone Number",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Enter valid E-mail",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.ic_vpn_key_black_24dp))
                .create().show();

    }
}
