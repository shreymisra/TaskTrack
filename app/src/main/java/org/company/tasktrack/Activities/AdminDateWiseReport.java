package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.company.tasktrack.Adapters.Admin.AdminDateWiseAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_date_wise_report);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Date Wise Report");
        tasks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tasks.setAdapter(new AdminDateWiseAdapter(getApplicationContext()));
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
