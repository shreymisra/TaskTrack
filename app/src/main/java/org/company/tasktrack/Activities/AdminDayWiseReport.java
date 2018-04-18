package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.company.tasktrack.Adapters.Admin.AdminDateWiseAdapter;
import org.company.tasktrack.Adapters.Admin.AdminDayWiseAdapter;
import org.company.tasktrack.Networking.Models.DayWiseReportReponse;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminDayWiseReport extends BaseActivity {

    @BindView(R.id.tasks)
    RecyclerView tasks;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.name)
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_day_wise_report);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Day Wise Report");

        DayWiseReportReponse response=gson.fromJson(getIntentExtras().getString("ReportResponse"),DayWiseReportReponse.class);
        String emp_id=getIntentExtras().getString("Emp_id");
        String emp_name=getIntentExtras().getString("Emp_name");
        String date2=getIntentExtras().getString("Date");
        name.setText(emp_name+" ("+ emp_id+")");
        date.setText(date2);


        tasks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tasks.setAdapter(new AdminDayWiseAdapter(getApplicationContext(),response));
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
