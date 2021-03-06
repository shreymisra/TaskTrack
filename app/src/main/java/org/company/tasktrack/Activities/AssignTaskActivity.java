package org.company.tasktrack.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import org.company.tasktrack.Networking.Models.AssignTaskModel;
import org.company.tasktrack.Networking.Models.AssignTaskResponse;
import org.company.tasktrack.Networking.Models.EmployeesUnderManagerModel;
import org.company.tasktrack.Networking.Models.EmployeesUnderManagerResponse;
import org.company.tasktrack.Networking.Models.UserInfoResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.AssignTask;
import org.company.tasktrack.Networking.Services.EmployeesUnderManager;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignTaskActivity extends BaseActivity {

    @BindView(R.id.employee)
    Spinner employee;
    @BindView(R.id.taskTitleLayout)
    TextInputLayout taskTitleLayout;
    @BindView(R.id.taskTitle)
    EditText taskTitle;
    @BindView(R.id.taskDescLayout)
    TextInputLayout taskDescLayout;
    @BindView(R.id.taskDesc)
    EditText taskDesc;
    @BindView(R.id.remarkLayout)
    TextInputLayout remarkLayout;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.taskDeadlineLayout)
    TextInputLayout deadlineLayout;
    @BindView(R.id.deadline)
    EditText deadline;
    Gson gson;
    EmployeesUnderManagerResponse managerResponse;
    @BindView(R.id.filterChips)
    LinearLayout filterChips;
    ChipCloud chipCloud;
    ProgressDialog progressDialog;
    HashMap<String,Integer> hm=new HashMap<String,Integer>();
    ArrayList<String> employeeList=new ArrayList<String>();
    AssignTaskModel assign;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    UserInfoResponse infoResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Assign Your Task");
        gson=new Gson();
        assign=new AssignTaskModel();
        progressDialog=new ProgressDialog(this);
         infoResponse=gson.fromJson(DbHandler.getString(getApplicationContext(),"UserInfo",""),UserInfoResponse.class);

        if(!DbHandler.contains(getApplicationContext(),"EmployeesUnderMe")) {
            EmployeesUnderManagerModel object = new EmployeesUnderManagerModel();
            object.setEmp_id(String.valueOf(infoResponse.getData().getEmpId()));
            EmployeesUnderManager employeesUnderManager = ServiceGenerator.createService(EmployeesUnderManager.class, DbHandler.getString(getApplicationContext(), "bearer", ""));
            Call<EmployeesUnderManagerResponse> call = employeesUnderManager.employees(object);
            call.enqueue(new Callback<EmployeesUnderManagerResponse>() {
                @Override
                public void onResponse(Call<EmployeesUnderManagerResponse> call, Response<EmployeesUnderManagerResponse> response) {
                    managerResponse = response.body();
                    //swipeRefresh.setRefreshing(false);
                    if (response.code() == 200) {
                        if (managerResponse.getSuccess()) {
                           DbHandler.putString(getApplicationContext(),"EmployeesUnderMe",gson.toJson(managerResponse));
                            processWork();
                             } else {
                            Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 403) {
                        DbHandler.unsetSession(getApplicationContext(), "isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<EmployeesUnderManagerResponse> call, Throwable t) {
                    handleNetworkErrors(t, -1);
                }
            });
        }
        else{

            managerResponse=gson.fromJson(DbHandler.getString(getApplicationContext(),"EmployeesUnderMe",""),EmployeesUnderManagerResponse.class);
           // Toast.makeText(getApplicationContext(),gson.toJson(managerResponse),Toast.LENGTH_SHORT).show();
            processWork();
        }

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)
                .selectMode(ChipCloud.SelectMode.mandatory)
                .checkedChipColor(getResources().getColor(R.color.colorAccent))
                .checkedTextColor(getResources().getColor(R.color.white))
                .uncheckedChipColor(getResources().getColor(R.color.chipUnselected))
                .uncheckedTextColor(getResources().getColor(R.color.dark_icon))
                .useInsetPadding(true);
        chipCloud = new ChipCloud(getApplicationContext(), filterChips, config);
        String[] chips = {"Low Priority", "Average Priority", "High Priority"};
        chipCloud.addChips(chips);
        chipCloud.setChecked(0);
        prioritySelected(0);
        deadline.setText("");

        chipCloud.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if (b1) {
                    prioritySelected(i);
                }
            }
        });

        hm.put(infoResponse.getData().getName(),infoResponse.getData().getEmpId());
        employeeList.add(infoResponse.getData().getName());
    }

    public void prioritySelected(int n){
        assign.setPriority(String.valueOf(n));
    }

    @OnTextChanged(R.id.taskTitle)
    public void setTaskTitle(){
        if(taskTitle.getText().toString().equals(""))
        {
            taskTitleLayout.setErrorEnabled(true);
            taskTitleLayout.setError("Field Required");
        }
        else{
            taskTitleLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.taskDesc)
    public void setTaskDesc(){
        if(taskDesc.getText().toString().equals(""))
        {
            taskDescLayout.setErrorEnabled(true);
            taskDescLayout.setError("Field Required");
        }
        else{
            taskDescLayout.setErrorEnabled(false);
        }

    }

    @OnTextChanged(R.id.remark)
    public void setRemark(){
        if(remark.getText().toString().equals(""))
        {
            remarkLayout.setErrorEnabled(true);
            remarkLayout.setError("Field Required");
        }
        else{
            remarkLayout.setErrorEnabled(false);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.assign_task, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send:
                submitTask();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submitTask(){
        if(assign.getTo().equals(infoResponse.getData().getEmpId()))
            DbHandler.remove(getApplicationContext(),"PendingTasks");
        assign.setName(taskTitle.getText().toString());
        assign.setDesc(taskDesc.getText().toString());
        assign.setManagerRemark(remark.getText().toString());
        if(assign.getName().equals("")||assign.getDesc().equals("")||assign.getManagerRemark().equals("")||assign.getTo().equals("")||assign.getPriority().equals("-1")||assign.getDeadline().equals("")){
            Toast.makeText(getApplicationContext(),"Please fill all the fields.",Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Uploading Task ...");
            progressDialog.setTitle("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //Toast.makeText(getApplicationContext(),"Uploading Task ...",Toast.LENGTH_LONG).show();
            AssignTask assignTask=ServiceGenerator.createService(AssignTask.class,DbHandler.getString(getApplicationContext(),"bearer",""));
            Call<AssignTaskResponse> call=assignTask.response(assign);
            call.enqueue(new Callback<AssignTaskResponse>() {
                @Override
                public void onResponse(Call<AssignTaskResponse> call, Response<AssignTaskResponse> response) {
                    progressDialog.dismiss();
                    if(response.code()==200)
                    {
                        AssignTaskResponse taskResponse=response.body();
                        if(taskResponse.getSuccess()){
                            DbHandler.remove(getApplicationContext(),"AssignedTasks");
                            Toast.makeText(getApplicationContext(),taskResponse.getMsg(),Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AssignTaskActivity.this,ManagerActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),taskResponse.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<AssignTaskResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        handleNetworkErrors(t,1);
                }
            });
        }
    }

    public void processWork()
    {
        for(int i=0;i<managerResponse.getEmployees().size();i++){
            hm.put(managerResponse.getEmployees().get(i).getName(),managerResponse.getEmployees().get(i).getEmpId());
            employeeList.add(managerResponse.getEmployees().get(i).getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, employeeList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        employee.setAdapter(adapter);
        employee.setPrompt("Select an employee");

        employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assign.setTo(String.valueOf(hm.get(employeeList.get(i))));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @OnClick(R.id.deadline)
    public void setDeadline(){
        deadline.setEnabled(false);
        deadline.setClickable(false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                deadline.setClickable(true);
                deadline.setEnabled(true);
            }
        }, 500);

        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                setTime(i,i1+1,i2);
            }
        },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
        datePickerDialog.show();
    }

    public void  setTime(int y,int m,int d){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second=c.get(Calendar.SECOND);
        String year=String.valueOf(y);
        String month=String.valueOf(m);
        String date=String.valueOf(d);
        if(month.length()==1)
            month="0"+month;
        if(date.length()==1)
            date="0"+date;
        final String datePicked=year+"-"+month+"-"+date;

        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String c=datePicked+" "+i+":"+i1+":00";
                String deadlineStr=datePicked+" "+i+":"+i1+":00";
                try {
                    assign.setDeadline(sdf.parse(deadlineStr).toString());
                   // Toast.makeText(AssignTaskActivity.this,sdf.parse(deadlineStr).toString(),Toast.LENGTH_SHORT).show();
                    deadline.setText(c);
                }
                catch(Exception e){
                    e.printStackTrace();
                }


            }
        },hour,minute,false);
        timePickerDialog.show();

    }
}
