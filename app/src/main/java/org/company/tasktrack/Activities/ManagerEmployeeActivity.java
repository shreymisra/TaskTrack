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
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<10;i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText("Shrey Misra");
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_forever_black_24dp, 0, 0, 0);
            existingEmployees.addView(tv);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employeesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addEmployeeSpinner.setAdapter(adapter);
        addEmployeeSpinner.setPrompt("Select Employee");
        addEmployeeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = new TextView(getApplicationContext());
                tv.setLayoutParams(lparams);
                tv.setText(employeesList.get(i));
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_forever_black_24dp, 0, 0, 0);
                existingEmployees.addView(tv);
            }
        });
    }
}
