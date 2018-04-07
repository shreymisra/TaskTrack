package org.company.tasktrack.Fragments.Admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.Adapters.Admin.ManagersAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Manage_Manager extends BaseFragment {

    View view;
    @BindView(R.id.managersList)
    RecyclerView managerList;
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
        managerList.setLayoutManager(new LinearLayoutManager(getContext()));
        managerList.setAdapter(new ManagersAdapter(getContext()));
        return view;
    }

}
