package org.company.tasktrack.Fragments.Admin;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Activities.AssignTaskActivity;
import org.company.tasktrack.Adapters.Admin.EmployeesAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.GetAllEmployees;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Manage_Employee extends BaseFragment {

    View view;
    @BindView(R.id.employeeList)
    RecyclerView employeeList;
    Gson gson;
    @BindView(R.id.assignTask)
    FloatingActionButton assignTask;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    int flag=0;
    public static Manage_Employee newInstance(String param1, String param2) {
        Manage_Employee fragment = new Manage_Employee();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_manage__employee, container, false);
        ButterKnife.bind(this,view);
        gson=new Gson();

        if(!DbHandler.contains(getContext(),"Employees")){
            fetchData();
        }else {
            GetAllEmployeesResponse allEmployees=gson.fromJson(DbHandler.getString(getContext(),"Employees",""),GetAllEmployeesResponse.class);
            employeeList.setLayoutManager(new LinearLayoutManager(getContext()));
            employeeList.setAdapter(new EmployeesAdapter(getContext(),allEmployees));
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                flag=1;
            }
        });
        return view;
    }

    public void fetchData(){
        GetAllEmployees employees = ServiceGenerator.createService(GetAllEmployees.class, DbHandler.getString(getContext(), "bearer", ""));
        Call<GetAllEmployeesResponse> employeesResponse = employees.getAllEmployees();
        employeesResponse.enqueue(new Callback<GetAllEmployeesResponse>() {
            @Override
            public void onResponse(Call<GetAllEmployeesResponse> call, Response<GetAllEmployeesResponse> response) {
                    swipeRefresh.setRefreshing(false);
                if (response.code() == 200) {
                    GetAllEmployeesResponse allEmployees = response.body();
                    if (allEmployees.getSuccess()) {
                        DbHandler.putString(getContext(), "Employees", gson.toJson(allEmployees));
                        employeeList.setLayoutManager(new LinearLayoutManager(getContext()));
                        employeeList.setAdapter(new EmployeesAdapter(getContext(),allEmployees));
                        if(flag==1)
                        {
                            Toast.makeText(getContext(),"Data Refreshed",Toast.LENGTH_SHORT).show();
                            flag=0;
                        }
                    }
                        else
                        Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_LONG).show();
                } else if (response.code() == 403) {
                    DbHandler.unsetSession(getContext(), "isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<GetAllEmployeesResponse> call, Throwable t) {
                handleNetworkErrors(t, -1);
            }
        });
    }

    @OnClick(R.id.assignTask)
    public void setAssignTask(){
        intentWithoutFinish(AssignTaskActivity.class);
    }
}
