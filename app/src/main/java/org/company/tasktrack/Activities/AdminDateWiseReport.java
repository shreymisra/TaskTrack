package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.company.tasktrack.Adapters.Admin.AdminDateWiseAdapter;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminDateWiseReport extends BaseActivity {

    @BindView(R.id.tasks)
    RecyclerView tasks;
    @BindView(R.id.fromDate)
    TextView fromDate;
    @BindView(R.id.toDate)
    TextView toDate;
    @BindView(R.id.empName)
    TextView empName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_date_wise_report);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Date Wise Report");
        GetAssignedTaskResponse response=gson.fromJson(getIntentExtras().getString("AssignedTaskResponse"),GetAssignedTaskResponse.class);
        String emp_id=getIntentExtras().getString("Emp_id");
        String emp_name=getIntentExtras().getString("Emp_name");
        String from=getIntentExtras().getString("From");
        String to=getIntentExtras().getString("To");
        empName.setText(emp_name+" ("+ emp_id+")");
        fromDate.setText(from);
        toDate.setText(to);

        tasks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tasks.setAdapter(new AdminDateWiseAdapter(this,response));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);

    }
}
