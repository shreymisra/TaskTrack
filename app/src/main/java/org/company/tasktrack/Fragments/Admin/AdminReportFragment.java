package org.company.tasktrack.Fragments.Admin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.company.tasktrack.Activities.AdminDateWiseReport;
import org.company.tasktrack.Activities.AdminDayWiseReport;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.SelectDateFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminReportFragment extends BaseFragment {

    @BindView(R.id.empName1)
    Spinner empName1;
    @BindView(R.id.fromDate)
    EditText fromDate;
    @BindView(R.id.toDate)
    EditText toDate;
    @BindView(R.id.dateWise)
    Button dateWise;
    @BindView(R.id.empName2)
    Spinner empName2;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.dayWise)
    Button dayWise;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SelectDateFragment dateFragment;
    ArrayList<String> employeesList=new ArrayList<String>();
    View view;
    public static AdminReportFragment newInstance(String param1, String param2) {
        AdminReportFragment fragment = new AdminReportFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this,view);

        employeesList.add("Shrey Misra");
        employeesList.add("Shivam Chaurasia");
        employeesList.add("Suyash Yadav");
        employeesList.add("Sooraj Shingari");
        Calendar from = Calendar.getInstance();
      Calendar to = Calendar.getInstance();

        fromDate.setText(sdf.format(from.getTime()));
        toDate.setText(sdf.format(to.getTime()));
        date.setText(sdf.format(from.getTime()));
        fromDate.setLongClickable(false);
        toDate.setLongClickable(false);
        date.setLongClickable(false);
        dateFragment = new SelectDateFragment();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, employeesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        empName1.setAdapter(adapter);
        empName1.setPrompt("Select Employee");
        empName1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        empName2.setAdapter(adapter);
        empName2.setPrompt("Select Employee");
       empName2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

        return view;
    }

    @OnClick(R.id.date)
    public void selectDate(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        date.setClickable(false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                date.setClickable(true);
            }
        }, 500);
        dateFragment.show(getFragmentManager(),sdf.format(calendar.getTime()), date, "2018-01-01", sdf.format(calendar.getTime()));

    }

    @OnClick(R.id.fromDate)
    public void setFromDate(){

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        //Toast.makeText(getContext(),sdf.format(calendar.getTime()),Toast.LENGTH_SHORT).show();
        fromDate.setClickable(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                toDate.setClickable(true);
            }
        }, 500);
        dateFragment.show(getFragmentManager(),sdf.format(calendar.getTime()), fromDate, "2018-01-01", "2050-01-01");

    }

    @OnClick(R.id.toDate)
    public void setToDate(){
        toDate.setClickable(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                toDate.setClickable(true);
            }
        }, 500);
        dateFragment.show(getFragmentManager(), toDate.getText().toString(), toDate, fromDate.getText().toString(), "2050-01-01");

    }


    @OnClick(R.id.dateWise)
    public void dateWiseReport(){
        intentWithoutFinish(AdminDateWiseReport.class);
    }


    @OnClick(R.id.dayWise)
    public void dayWiseReport(){
        intentWithoutFinish(AdminDayWiseReport.class);
    }
}
