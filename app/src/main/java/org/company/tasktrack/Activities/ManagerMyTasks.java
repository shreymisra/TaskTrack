package org.company.tasktrack.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class ManagerMyTasks extends BaseActivity {

    @BindView(R.id.taskList)
    RecyclerView taskList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    GetAssignedTaskResponse taskResponse;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_my_tasks);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Your Tasks");
        taskResponse=new GetAssignedTaskResponse();
        gson=new Gson();
        progressDialog=new ProgressDialog(this);

        //if(!DbHandler.contains(this,"PendingTasks")){
            progressDialog.setTitle("Loading Tasks");
            progressDialog.setMessage("Please wait until we fetch your tasks ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fetchData();
        /*} else{
            taskResponse=gson.fromJson(DbHandler.getString(this,"PendingTasks",""),GetAssignedTaskResponse.class);
            updateRecyclerView(taskResponse);
        }*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentWithFinish(ManagerActivity.class);
       // this.finishAffinity();
    }


    public void fetchData(){

        PendingTasks pendingTasks= ServiceGenerator.createService(PendingTasks.class,DbHandler.getString(this,"bearer",""));
        Call<GetAssignedTaskResponse> call=pendingTasks.responseTasks();
        call.enqueue(new Callback<GetAssignedTaskResponse>() {
            @Override
            public void onResponse(Call<GetAssignedTaskResponse> call, Response<GetAssignedTaskResponse> response) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){
                    taskResponse=response.body();
                    if(taskResponse.getSuccess()){
                        DbHandler.putString(getApplicationContext(),"PendingTasks",gson.toJson(taskResponse));
                        updateRecyclerView(taskResponse);
                    }
                    else{
                        Toast.makeText(ManagerMyTasks.this,"Some Error Occured",Toast.LENGTH_LONG).show();
                    }
                }else{
                    DbHandler.unsetSession(ManagerMyTasks.this,"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<GetAssignedTaskResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                handleNetworkErrors(t,-1);
            }
        });
    }


    public void updateRecyclerView(GetAssignedTaskResponse response){
        if(response.getTasks().size()!=0) {
            taskList.setVisibility(View.VISIBLE);
            taskList.setLayoutManager(new LinearLayoutManager(this));
            taskList.setAdapter(new EmployeeTaskListAdapter(this, response,1));
        }
        else{
            taskList.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
