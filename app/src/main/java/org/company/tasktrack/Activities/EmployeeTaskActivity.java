package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import org.company.tasktrack.Adapters.EmployeeTaskImagesAdapter;
import org.company.tasktrack.R;

import butterknife.BindView;

public class EmployeeTaskActivity extends AppCompatActivity {

    @BindView(R.id.taskTitle)
    TextView taskTitle;
    @BindView(R.id.taskDesc)
    TextView taskDesc;
    @BindView(R.id.imagesList)
    RecyclerView imagesList;
    @BindView(R.id.taskCompleted)
    Button taskCompleted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imagesList.setLayoutManager(new GridLayoutManager(this,3));
        imagesList.setAdapter(new EmployeeTaskImagesAdapter());
    }
}
