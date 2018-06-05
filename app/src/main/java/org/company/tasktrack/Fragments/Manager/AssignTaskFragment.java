package org.company.tasktrack.Fragments.Manager;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Activities.AssignTaskActivity;
import org.company.tasktrack.Activities.ManagerMyTasks;
import org.company.tasktrack.Adapters.Manager.ManagerAssignedTasksAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.Networking.Models.AssignTaskResponse;
import org.company.tasktrack.Networking.Models.GetAssignedTaskModel;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.GetAssignedTasks;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignTaskFragment extends BaseFragment {


    @BindView(R.id.assignedTasks)
    RecyclerView assignedTasks;
    @BindView(R.id.assignTask)
    FloatingActionButton assignTask;

    @BindView(R.id.myTasks)
    CardView myTasks;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    View view;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    GetAssignedTaskResponse assignedTaskResponse;
    Gson gson;
    public static AssignTaskFragment newInstance(String param1, String param2) {
        AssignTaskFragment fragment = new AssignTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_assign_task, container, false);
        ButterKnife.bind(this,view);
        gson=new Gson();
        if(!DbHandler.contains(getContext(),"AssignedTasks")){
                fetchData();
        }else{
           assignedTaskResponse=gson.fromJson(DbHandler.getString(getContext(),"AssignedTasks",""), GetAssignedTaskResponse.class);
            updateRecyclerView(assignedTaskResponse);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                DbHandler.remove(getContext(),"AssignedTasks");
                fetchData();
            }
        });

        assignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), AssignTaskActivity.class));
            }
        });
        return view;
    }

    public void updateRecyclerView(GetAssignedTaskResponse response){
        assignedTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        assignedTasks.setAdapter(new ManagerAssignedTasksAdapter(getContext(),response));
    }

    public void fetchData(){
        Calendar today = Calendar.getInstance();
        GetAssignedTaskModel object=new GetAssignedTaskModel();
        object.setEmpId("-1");
        object.setFdate(sdf.format(today.getTime()));
        object.setTdate(sdf.format(today.getTime()));
        GetAssignedTasks assignedTaskService= ServiceGenerator.createService(GetAssignedTasks.class,DbHandler.getString(getContext(),"bearer",""));
        Call<GetAssignedTaskResponse> call=assignedTaskService.responseTasks(object);
        call.enqueue(new Callback<GetAssignedTaskResponse>() {
            @Override
            public void onResponse(Call<GetAssignedTaskResponse> call, Response<GetAssignedTaskResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){
                    assignedTaskResponse=response.body();
                    if(assignedTaskResponse.getSuccess()){
                        DbHandler.putString(getContext(),"AssignedTasks",gson.toJson(assignedTaskResponse));
                        updateRecyclerView(assignedTaskResponse);
                    }
                    else{
                        Toast.makeText(getContext(),"Some Error Occured", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    DbHandler.unsetSession(getContext(),"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<GetAssignedTaskResponse> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.myTasks)
    public void myTask()
    {
        intentWithoutFinish(ManagerMyTasks.class);
    }
}
