package org.company.tasktrack.Fragments.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.Adapters.Admin.EmployeesAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Manage_Employee extends BaseFragment {

    View view;
    @BindView(R.id.employeeList)
    RecyclerView employeeList;

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
        employeeList.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeList.setAdapter(new EmployeesAdapter(getContext()));
        return view;
    }

}
