package org.company.tasktrack.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.company.tasktrack.Application.Config;
import org.company.tasktrack.Fragments.Admin.AddEmployeeFragment;
import org.company.tasktrack.Fragments.Admin.ManageFragment;
import org.company.tasktrack.Fragments.Admin.AdminReportFragment;
import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.Networking.Models.GetAllManagersResponse;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.GetAllEmployees;
import org.company.tasktrack.Networking.Services.GetAllManagers;
import org.company.tasktrack.Networking.Services.UserInfo;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.BottomNavigationViewHelper;
import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.NotificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class AdminActivity extends BaseActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.container)
    FrameLayout container;
    Gson gson;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_add:
                replaceFragment(new AddEmployeeFragment());
                break;
            case R.id.navigation_manage:
                replaceFragment(new ManageFragment());
                break;
            case R.id.navigation_reports:
                replaceFragment(new AdminReportFragment());
                break;
        }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        Log.e("id", FirebaseInstanceId.getInstance().getToken());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        //UserInfoResponse response=gson.fromJson(DbHandler.getString(getApplicationContext(),"UserInfo",""),UserInfoResponse.class);
        gson=new Gson();
        replaceFragment(new AddEmployeeFragment());
        getSupportFragmentManager()
                .addOnBackStackChangedListener(
                        () -> updateBottomNavigationTitle(getSupportFragmentManager().findFragmentById(R.id.container))
                );

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //DbHandler.putInt(DashboardActivity.this, "count", 1);
                //invalidateOptionsMenu();
            }
        };
        if(!DbHandler.contains(getApplicationContext(),"Managers")) {
            GetAllManagers managers = ServiceGenerator.createService(GetAllManagers.class, DbHandler.getString(getApplicationContext(), "bearer", ""));
            Call<GetAllManagersResponse> managersResponse = managers.getAllManagers();
            managersResponse.enqueue(new Callback<GetAllManagersResponse>() {
                @Override
                public void onResponse(Call<GetAllManagersResponse> call, Response<GetAllManagersResponse> response) {
                    if (response.code() == 200) {
                        GetAllManagersResponse allManagers = response.body();

                        if (allManagers.getSuccess())
                            DbHandler.putString(getApplicationContext(), "Managers", gson.toJson(allManagers));
                        else
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();

                    } else if (response.code() == 403) {
                        DbHandler.unsetSession(getApplicationContext(), "isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<GetAllManagersResponse> call, Throwable t) {
                    handleNetworkErrors(t, -1);
                }
            });
        }

        if(!DbHandler.contains(getApplicationContext(),"Employees")) {
            GetAllEmployees employees = ServiceGenerator.createService(GetAllEmployees.class, DbHandler.getString(getApplicationContext(), "bearer", ""));
            Call<GetAllEmployeesResponse> employeesResponse = employees.getAllEmployees();
            employeesResponse.enqueue(new Callback<GetAllEmployeesResponse>() {
                @Override
                public void onResponse(Call<GetAllEmployeesResponse> call, Response<GetAllEmployeesResponse> response) {
                    if (response.code() == 200) {
                        GetAllEmployeesResponse allEmployees = response.body();
                        if (allEmployees.getSuccess())
                            DbHandler.putString(getApplicationContext(), "Employees", gson.toJson(allEmployees));
                        else
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 403) {
                        DbHandler.unsetSession(getApplicationContext(), "isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<GetAllEmployeesResponse> call, Throwable t) {
                    handleNetworkErrors(t, -1);
                }
            });
        }

    }

    public void updateBottomNavigationTitle(Fragment f) {
        String className = f.getClass().getName();
        if (className.equals(AddEmployeeFragment.class.getName()))
            navigation.getMenu().getItem(0).setChecked(true);
        else if (className.equals(ManageFragment.class.getName()))
            navigation.getMenu().getItem(1).setChecked(true);
        else if (className.equals(AdminReportFragment.class.getName()))
            navigation.getMenu().getItem(2).setChecked(true);
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
           this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.notifications:
                intentWithoutFinish(NotificationsActivity.class);
                break;
            case R.id.account:
                intentWithoutFinish(AccountActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(getApplicationContext());
    }

}
