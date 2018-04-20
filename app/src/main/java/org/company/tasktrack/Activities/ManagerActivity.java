package org.company.tasktrack.Activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.company.tasktrack.Fragments.Admin.AddEmployeeFragment;
import org.company.tasktrack.Fragments.Admin.ManageFragment;
import org.company.tasktrack.Fragments.Manager.AssignTaskFragment;
import org.company.tasktrack.Fragments.Manager.ManagerReportFragment;
import org.company.tasktrack.Networking.Models.CheckAttendanceStatusResponse;
import org.company.tasktrack.Networking.Models.FcmIdModel;
import org.company.tasktrack.Networking.Models.FcmIdResponse;
import org.company.tasktrack.Networking.Models.MarkAttendanceResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.CheckAttendanceStatus;
import org.company.tasktrack.Networking.Services.MarkAttendance;
import org.company.tasktrack.Networking.Services.SendFcmId;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.BottomNavigationViewHelper;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerActivity extends BaseActivity {

    private TextView mTextMessage;

    @BindView(R.id.l2)
    LinearLayout out;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.markAttendance)
    Button markAttButton;
    @BindView(R.id.text2)
    TextView text2;
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
                case R.id.markout:
                    markAttendance2();
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

        if(!DbHandler.contains(getApplicationContext(),"AttendanceStatus")) {
            getAttendanceStatus();
        }
        else{
            int status=DbHandler.getInt(getApplicationContext(),"AttendanceStatus",2);
            adjustLayout(status);
        }

        refreshFcmId();
    }

    public void adjustLayout(int status){
        if(status==0){
            out.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);
            text2.setText("You haven't signed in yet .");
            markAttButton.setEnabled(true);
        }
        else if(status==1)
        {
            navigation.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
            out.setVisibility(View.GONE);

            replaceFragment(new AssignTaskFragment());
            getSupportFragmentManager()
                    .addOnBackStackChangedListener(
                            () -> updateBottomNavigationTitle(getSupportFragmentManager().findFragmentById(R.id.container))
                    );
            //navigation.getMenu().getItem(0).setChecked(true);
        }
        else if(status==2)
        {
            out.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);
            text2.setText("You have successfully marked out for today .");
            markAttButton.setBackgroundColor(getResources().getColor(R.color.viewColor));
            markAttButton.setEnabled(false);
        }
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

    public void getAttendanceStatus(){

        CheckAttendanceStatus checkStatus = ServiceGenerator.createService(CheckAttendanceStatus.class, DbHandler.getString(this, "bearer", ""));
        Call<CheckAttendanceStatusResponse> call = checkStatus.check();
        call.enqueue(new Callback<CheckAttendanceStatusResponse>() {
            @Override
            public void onResponse(Call<CheckAttendanceStatusResponse> call, Response<CheckAttendanceStatusResponse> response) {
                if (response.code() == 200) {
                    CheckAttendanceStatusResponse statusResponse = response.body();
                    DbHandler.putInt(getApplicationContext(),"AttendanceStatus",statusResponse.getCount());
                    adjustLayout(statusResponse.getCount());
                } else {
                    DbHandler.unsetSession(getApplicationContext(), "isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<CheckAttendanceStatusResponse> call, Throwable t) {
                handleNetworkErrors(t, 1);
            }
        });
    }

    @OnClick(R.id.markAttendance)
    public void markAttendance(){
        markAttButton.setEnabled(false);
        MarkAttendance markAttendance=ServiceGenerator.createService(MarkAttendance.class,DbHandler.getString(this,"bearer",""));
        Call<MarkAttendanceResponse> call=markAttendance.mark();
        call.enqueue(new Callback<MarkAttendanceResponse>() {
            @Override
            public void onResponse(Call<MarkAttendanceResponse> call, Response<MarkAttendanceResponse> response) {
                markAttButton.setEnabled(true);
                if(response.code()==200)
                {
                    MarkAttendanceResponse attendanceResponse=response.body();
                    if(attendanceResponse.isSuccess()){
                        Toast.makeText(getApplicationContext(),attendanceResponse.getMsg(),Toast.LENGTH_LONG).show();
                        DbHandler.remove(getApplicationContext(),"AttendanceStatus");
                        /*if(attendanceResponse.getCount()==1){
                            out.setVisibility(View.GONE);
                            in.setVisibility(View.VISIBLE);
                        }
                        else{
                            out.setVisibility(View.VISIBLE);
                            in.setVisibility(View.GONE);
                        }*/
                        getAttendanceStatus();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),attendanceResponse.getMsg(),Toast.LENGTH_LONG).show();
                    }
                }else{
                    DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<MarkAttendanceResponse> call, Throwable t) {
                handleNetworkErrors(t,1);
                markAttButton.setEnabled(true);
            }
        });
    }


    public void markAttendance2(){
        new AlertDialog.Builder(this)
                .setMessage("Once You Logout , you won't be able to login again for the day.")
                .setTitle("Are You Sure ?")
                .setPositiveButton("Mark Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        markAttendance();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

    }

}
