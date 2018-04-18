package org.company.tasktrack.Activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.company.tasktrack.Adapters.Employee.EmployeeTaskListAdapter;
import org.company.tasktrack.Networking.Models.CheckAttendanceStatusResponse;
import org.company.tasktrack.Networking.Models.FcmIdModel;
import org.company.tasktrack.Networking.Models.FcmIdResponse;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.Networking.Models.MarkAttendanceResponse;
import org.company.tasktrack.Networking.Models.RequestTaskResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.CheckAttendanceStatus;
import org.company.tasktrack.Networking.Services.MarkAttendance;
import org.company.tasktrack.Networking.Services.PendingTasks;
import org.company.tasktrack.Networking.Services.RequestTaskService;
import org.company.tasktrack.Networking.Services.SendFcmId;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends BaseActivity {

    @BindView(R.id.taskList)
    RecyclerView taskList;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.l1)
    RelativeLayout in;
    @BindView(R.id.l2)
    LinearLayout out;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.markAttendance)
    Button markAttButton;
    @BindView(R.id.request)
    Button request;
    Gson gson;
    GetAssignedTaskResponse taskResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        refreshFcmId();
        taskResponse=new GetAssignedTaskResponse();
        gson=new Gson();

        if(!DbHandler.contains(getApplicationContext(),"AttendanceStatus")) {
           getAttendanceStatus();
        }
        else{
            int status=DbHandler.getInt(getApplicationContext(),"AttendanceStatus",2);
            adjustLayout(status);
        }

        fetchData();

        if(!DbHandler.contains(this,"PendingTasks")){
            fetchData();
        } else{
            taskResponse=gson.fromJson(DbHandler.getString(this,"PendingTasks",""),GetAssignedTaskResponse.class);
            updateRecyclerView(taskResponse);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

    }

    @OnClick(R.id.request)
    public void requestTask(){
        RequestTaskService requestTaskService=ServiceGenerator.createService(RequestTaskService.class,DbHandler.getString(getApplicationContext(),"bearer",""));
        Call<RequestTaskResponse> call=requestTaskService.response();
        call.enqueue(new Callback<RequestTaskResponse>() {
            @Override
            public void onResponse(Call<RequestTaskResponse> call, Response<RequestTaskResponse> response) {
                if(response.code()==200){
                    RequestTaskResponse taskResponse=response.body();
                    Toast.makeText(getApplicationContext(),taskResponse.getMsg(),Toast.LENGTH_LONG).show();
                }else if(response.code()==403)
                {
                    DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<RequestTaskResponse> call, Throwable t) {

            }
        });
    }
    public void adjustLayout(int status){
        if (status == 0) {
            out.setVisibility(View.VISIBLE);
            in.setVisibility(View.GONE);
            text2.setText("You haven't signed in yet .");
            markAttButton.setEnabled(true);
        } else if (status == 1) {
            out.setVisibility(View.GONE);
            in.setVisibility(View.VISIBLE);
        } else if (status == 2) {
            out.setVisibility(View.VISIBLE);
            in.setVisibility(View.GONE);
            text2.setText("You have successfully marked out for today .");
            markAttButton.setBackgroundColor(getResources().getColor(R.color.viewColor));
            markAttButton.setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            this.finishAffinity();
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

    public void fetchData(){

            PendingTasks pendingTasks= ServiceGenerator.createService(PendingTasks.class,DbHandler.getString(this,"bearer",""));
            Call<GetAssignedTaskResponse> call=pendingTasks.responseTasks();
            call.enqueue(new Callback<GetAssignedTaskResponse>() {
                @Override
                public void onResponse(Call<GetAssignedTaskResponse> call, Response<GetAssignedTaskResponse> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if(response.code()==200){
                        taskResponse=response.body();
                        if(taskResponse.getSuccess()){
                            updateRecyclerView(taskResponse);
                        }
                        else{
                            Toast.makeText(EmployeeActivity.this,"Some Error Occured",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        DbHandler.unsetSession(EmployeeActivity.this,"isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<GetAssignedTaskResponse> call, Throwable t) {
                   swipeRefreshLayout.setRefreshing(false);
                    handleNetworkErrors(t,-1);
                }
            });
        }

    public void updateRecyclerView(GetAssignedTaskResponse response){
        if(response.getTasks().size()!=0) {
            request.setVisibility(View.GONE);
            taskList.setVisibility(View.VISIBLE);
            taskList.setLayoutManager(new LinearLayoutManager(this));
            taskList.setAdapter(new EmployeeTaskListAdapter(this, response));
        }
        else{
            request.setVisibility(View.VISIBLE);
            taskList.setVisibility(View.GONE);
        }
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

    @OnClick(R.id.markAttendanceOut)
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
    }


