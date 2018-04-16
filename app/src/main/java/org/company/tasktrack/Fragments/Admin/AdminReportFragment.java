package org.company.tasktrack.Fragments.Admin;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Activities.AdminDateWiseReport;
import org.company.tasktrack.Activities.AdminDayWiseReport;
import org.company.tasktrack.Adapters.Admin.EmployeesAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.Networking.Models.GetAssignedTaskModel;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.GetAllEmployees;
import org.company.tasktrack.Networking.Services.GetAssignedTasks;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.SelectDateFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    Gson gson;
    GetAssignedTaskModel model;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SelectDateFragment dateFragment;
    ArrayList<String> employeesList=new ArrayList<String>();
    HashMap<String,Integer> hm=new HashMap<String,Integer>();
    String empName="";
    View view;
    ProgressDialog progressDialog;
    GetAllEmployeesResponse allEmployees;
    public static AdminReportFragment newInstance(String param1, String param2) {
        AdminReportFragment fragment = new AdminReportFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_admin_report, container, false);
        ButterKnife.bind(this,view);
        model=new GetAssignedTaskModel();
        gson=new Gson();
        progressDialog=new ProgressDialog(getContext());
        employeesList=new ArrayList<String>();
        hm=new HashMap<String,Integer>();
        if(!DbHandler.contains(getContext(),"Employees")){
            fetchData();
        }else {
           allEmployees=gson.fromJson(DbHandler.getString(getContext(),"Employees",""),GetAllEmployeesResponse.class);
        }

        //GetAllEmployeesResponse allEmployees= gson.fromJson(DbHandler.getString(getContext(),"Employees",""),GetAllEmployeesResponse.class);
        for(int i=0;i<allEmployees.getEmployees().size();i++)
        {
            employeesList.add(allEmployees.getEmployees().get(i).getName());
            hm.put(allEmployees.getEmployees().get(i).getName(),allEmployees.getEmployees().get(i).getEmpId());
        }


        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        fromDate.setText(sdf.format(from.getTime()));
        toDate.setText(sdf.format(to.getTime()));
        date.setText(sdf.format(from.getTime()));
        //fromDate.setLongClickable(false);
        //toDate.setLongClickable(false);
        //date.setLongClickable(false);
        dateFragment = new SelectDateFragment();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, employeesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        empName1.setAdapter(adapter);
        empName1.setPrompt("Select Employee");
        empName1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                model.setEmpId(String.valueOf(hm.get(employeesList.get(i))));
                empName=employeesList.get(i);
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
        progressDialog.setTitle("Generating Report");
        progressDialog.setMessage("Please wait until we generate this report ... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        model.setTdate(toDate.getText().toString());
        model.setFdate(fromDate.getText().toString());

        GetAssignedTasks getAssignedTasks= ServiceGenerator.createService(GetAssignedTasks.class,DbHandler.getString(getContext(),"bearer",""));
        Call<GetAssignedTaskResponse> call=getAssignedTasks.responseTasks(model);
        call.enqueue(new Callback<GetAssignedTaskResponse>() {
            @Override
            public void onResponse(Call<GetAssignedTaskResponse> call, Response<GetAssignedTaskResponse> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    GetAssignedTaskResponse getAssignedTaskResponse=response.body();
                    if(getAssignedTaskResponse.getSuccess()){
                        getIntentExtras().putString("AssignedTaskResponse",gson.toJson(getAssignedTaskResponse));
                        getIntentExtras().putString("Emp_id",model.getEmpId());
                        getIntentExtras().putString("Emp_name",empName);
                        getIntentExtras().putString("From",fromDate.getText().toString());
                        getIntentExtras().putString("To",toDate.getText().toString());
                        intentWithoutFinish(AdminDateWiseReport.class);
                    }
                    else{
                        Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                    }


                }else if (response.code()==403){
                    DbHandler.unsetSession(getContext(),"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<GetAssignedTaskResponse> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.dayWise)
    public void dayWiseReport(){
        intentWithoutFinish(AdminDayWiseReport.class);
    }

    public void fetchData(){
        GetAllEmployees employees = ServiceGenerator.createService(GetAllEmployees.class, DbHandler.getString(getContext(), "bearer", ""));
        Call<GetAllEmployeesResponse> employeesResponse = employees.getAllEmployees();
        employeesResponse.enqueue(new Callback<GetAllEmployeesResponse>() {
            @Override
            public void onResponse(Call<GetAllEmployeesResponse> call, Response<GetAllEmployeesResponse> response) {
                if (response.code() == 200) {
                     allEmployees = response.body();
                    if (allEmployees.getSuccess()) {
                        DbHandler.putString(getContext(), "Employees", gson.toJson(allEmployees));
                    }
                    else
                        Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_LONG).show();
                } else if (response.code() == 403) {
                    DbHandler.unsetSession(getContext(), "isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<GetAllEmployeesResponse> call, Throwable t) {
                handleNetworkErrors(t, -1);
            }
        });
    }
}
