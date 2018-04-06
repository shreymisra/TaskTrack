package org.company.tasktrack.Activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import org.company.tasktrack.Adapters.EmployeeTaskListAdapter;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeActivity extends BaseActivity {

    @BindView(R.id.taskList)
    RecyclerView taskList;
    @BindView(R.id.form_profile_image)
    ImageView profileImage;
    @BindView(R.id.employeeName)
    TextView employeeName;
    @BindView(R.id.employeeId)
    TextView employeeId;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);

        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new EmployeeTaskListAdapter(this));
    }
}
