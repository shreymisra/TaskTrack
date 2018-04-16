package org.company.tasktrack.Fragments.Admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Adapters.Admin.ManagersAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.Networking.Models.GetAllManagersResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.GetAllManagers;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Manage_Manager extends BaseFragment {

    View view;
    @BindView(R.id.managersList)
    RecyclerView managerList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    Gson gson;
    int flag=0;
    public static Manage_Manager newInstance(String param1, String param2) {
        Manage_Manager fragment = new Manage_Manager();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_manage_manager, container, false);
        ButterKnife.bind(this,view);
        gson=new Gson();

        if(!DbHandler.contains(getContext(),"Managers")){
            fetchData();
        }
        else{
            GetAllManagersResponse allManagers=gson.fromJson(DbHandler.getString(getContext(),"Managers",""),GetAllManagersResponse.class);
            managerList.setLayoutManager(new LinearLayoutManager(getContext()));
            managerList.setAdapter(new ManagersAdapter(getContext(),allManagers));
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

    public  void fetchData(){
        GetAllManagers managers = ServiceGenerator.createService(GetAllManagers.class, DbHandler.getString(getContext(), "bearer", ""));
        Call<GetAllManagersResponse> managersResponse = managers.getAllManagers();
        managersResponse.enqueue(new Callback<GetAllManagersResponse>() {
            @Override
            public void onResponse(Call<GetAllManagersResponse> call, Response<GetAllManagersResponse> response) {
                swipeRefresh.setRefreshing(false);
                if (response.code() == 200) {
                    GetAllManagersResponse allManagers = response.body();

                    if (allManagers.getSuccess()) {
                        DbHandler.putString(getContext(), "Managers", gson.toJson(allManagers));
                        managerList.setLayoutManager(new LinearLayoutManager(getContext()));
                        managerList.setAdapter(new ManagersAdapter(getContext(),allManagers));
                        if(flag==1){
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
            public void onFailure(Call<GetAllManagersResponse> call, Throwable t) {
                handleNetworkErrors(t, -1);
            }
        });
    }

}
