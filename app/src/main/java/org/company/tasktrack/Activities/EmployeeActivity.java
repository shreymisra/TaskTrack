package org.company.tasktrack.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import org.company.tasktrack.Adapters.Employee.EmployeeTaskListAdapter;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeActivity extends BaseActivity {

    @BindView(R.id.taskList)
    RecyclerView taskList;

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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.account:
                intentWithoutFinish(AccountActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
