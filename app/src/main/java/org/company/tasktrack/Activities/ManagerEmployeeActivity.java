package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.company.tasktrack.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagerEmployeeActivity extends AppCompatActivity {

    @BindView(R.id.addEmployeeSpinner)
    Spinner addEmployeeSpinner;
    @BindView(R.id.existingEmployees)
    LinearLayout existingEmployees;
    ArrayList<String> employeesList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_employee);
        ButterKnife.bind(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employeesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addEmployeeSpinner.setAdapter(adapter);
        addEmployeeSpinner.setPrompt("Select Employee");
        addEmployeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
