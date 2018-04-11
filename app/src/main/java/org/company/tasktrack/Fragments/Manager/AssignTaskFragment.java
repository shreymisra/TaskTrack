package org.company.tasktrack.Fragments.Manager;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.Adapters.Manager.ManagerAssignedTasksAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignTaskFragment extends BaseFragment {


    @BindView(R.id.assignedTasks)
    RecyclerView assignedTasks;
    View view;
    public static AssignTaskFragment newInstance(String param1, String param2) {
        AssignTaskFragment fragment = new AssignTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_assign_task, container, false);
        ButterKnife.bind(this,view);
        assignedTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        assignedTasks.setAdapter(new ManagerAssignedTasksAdapter(getContext()));
        return view;
    }
}
