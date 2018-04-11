package org.company.tasktrack.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.UserInfo;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

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

    Gson gson;
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
        if(DbHandler.contains(getApplicationContext(),"UserInfo")){
            UserInfoResponse userInfo=gson.fromJson(DbHandler.getString(getApplicationContext(),"UserInfo",""), UserInfoResponse.class);
            role.setText(userInfo.getData().getEmpType());
            email.setText(userInfo.getData().getEmail());
            name.setText(userInfo.getData().getName()+" ("+userInfo.getData().getEmpId()+")");
        }
        else{
            UserInfo userInfo = ServiceGenerator.createService(UserInfo.class, DbHandler.getString(getApplicationContext(),"bearer",""));
            Call<UserInfoResponse> responseCall = userInfo.infoResponse();
            responseCall.enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    UserInfoResponse userInfoResponse = response.body();

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
        logout();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);

    }
}
