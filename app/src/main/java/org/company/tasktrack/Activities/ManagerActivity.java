package org.company.tasktrack.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.company.tasktrack.Fragments.Admin.AddEmployeeFragment;
import org.company.tasktrack.Fragments.Admin.ManageFragment;
import org.company.tasktrack.Fragments.Manager.AssignTaskFragment;
import org.company.tasktrack.Fragments.Manager.ManagerReportFragment;
import org.company.tasktrack.Networking.Models.FcmIdModel;
import org.company.tasktrack.Networking.Models.FcmIdResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.SendFcmId;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.BottomNavigationViewHelper;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerActivity extends BaseActivity {

    private TextView mTextMessage;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_assignTask:
                    replaceFragment(new AssignTaskFragment());
                    return true;
                case R.id.navigation_reports2:
                    replaceFragment(new ManagerReportFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        replaceFragment(new AssignTaskFragment());
        getSupportFragmentManager()
                .addOnBackStackChangedListener(
                        () -> updateBottomNavigationTitle(getSupportFragmentManager().findFragmentById(R.id.container))
                );

        refreshFcmId();
    }

    public void updateBottomNavigationTitle(Fragment f) {
        String className = f.getClass().getName();
        if (className.equals(AddEmployeeFragment.class.getName()))
            navigation.getMenu().getItem(0).setChecked(true);
        else if (className.equals(ManageFragment.class.getName()))
            navigation.getMenu().getItem(1).setChecked(true);
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
    public void refreshFcmId(){
        String fcm= FirebaseInstanceId.getInstance().getToken();
        if(!DbHandler.contains(this,"fcm_id")||!DbHandler.getString(this,"fcm_id","").equals(fcm)) {
            FcmIdModel object = new FcmIdModel();
            object.setFcm(fcm);
            SendFcmId sendFcmId = ServiceGenerator.createService(SendFcmId.class, DbHandler.getString(this, "bearer", ""));
            Call<FcmIdResponse> call = sendFcmId.sendId(object);
            call.enqueue(new Callback<FcmIdResponse>() {
                @Override
                public void onResponse(Call<FcmIdResponse> call, Response<FcmIdResponse> response) {
                    FcmIdResponse fcmIdResponse=response.body();
                    if(response.code()==200){
                        if(fcmIdResponse.isSuccess()){
                            DbHandler.putString(getApplicationContext(),"fcm_id",fcm);
                        }
                        else{

                        }

                    }
                    else{
                        DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                    }
                }
                @Override
                public void onFailure(Call<FcmIdResponse> call, Throwable t) {
                    handleNetworkErrors(t,-1);
                }
            });
        }
    }

}
