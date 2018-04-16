package org.company.tasktrack.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Adapters.Employee.EmployeeTaskListAdapter;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.PendingTasks;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends BaseActivity {

    @BindView(R.id.taskList)
    RecyclerView taskList;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    GetAssignedTaskResponse taskResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        taskResponse=new GetAssignedTaskResponse();
        gson=new Gson();
        fetchData();

        if(!DbHandler.contains(this,"PendingTasks")){
            fetchData();
        } else{
            taskResponse=gson.fromJson(DbHandler.getString(this,"PendingTasks",""),GetAssignedTaskResponse.class);
            updateRecyclerView(taskResponse);
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

    public void fetchData(){

            PendingTasks pendingTasks= ServiceGenerator.createService(PendingTasks.class,DbHandler.getString(this,"bearer",""));
            Call<GetAssignedTaskResponse> call=pendingTasks.responseTasks();
            call.enqueue(new Callback<GetAssignedTaskResponse>() {
                @Override
                public void onResponse(Call<GetAssignedTaskResponse> call, Response<GetAssignedTaskResponse> response) {
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
                    handleNetworkErrors(t,-1);
                }
            });
        }

    public void updateRecyclerView(GetAssignedTaskResponse response){
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new EmployeeTaskListAdapter(this,response));
    }

    }

