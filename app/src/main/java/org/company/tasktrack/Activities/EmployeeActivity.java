package org.company.tasktrack.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.company.tasktrack.Adapters.EmployeeTaskListAdapter;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeActivity extends BaseActivity {

    @BindView(R.id.taskList)
    RecyclerView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new EmployeeTaskListAdapter());
    }
}
