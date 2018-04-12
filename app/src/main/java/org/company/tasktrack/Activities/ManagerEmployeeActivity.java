package org.company.tasktrack.Activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import org.company.tasktrack.Adapters.Admin.EmployeesUnderManagerAdapter;
import org.company.tasktrack.Networking.Models.AddEmployeesUnderManagerModel;
import org.company.tasktrack.Networking.Models.AddEmployeesUnderManagerResponse;
import org.company.tasktrack.Networking.Models.EmployeesUnderManagerModel;
import org.company.tasktrack.Networking.Models.EmployeesUnderManagerResponse;
import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.AddEmployeeUnderManager;
import org.company.tasktrack.Networking.Services.EmployeesUnderManager;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerEmployeeActivity extends BaseActivity {

    @BindView(R.id.addEmployeeSpinner)
    Spinner addEmployeeSpinner;
    @BindView(R.id.existingEmployees)
    RecyclerView existingEmployees;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    ArrayList<String> employeesList=new ArrayList<String>();
    HashMap<String,Integer> hm=new HashMap<String,Integer>();

    EmployeesUnderManagerResponse managerResponse;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_employee);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        gson=new Gson();
        Bundle b=getIntent().getExtras();
        String man_id=b.getString("manager_id");
        refreshData(man_id);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(man_id);
            }
        });



        employeesList.add("Choose an Employee");
        hm.put("Choose an Employee",-1);
        GetAllEmployeesResponse allEmployees=gson.fromJson(DbHandler.getString(getApplicationContext(),"Employees",""),GetAllEmployeesResponse.class);
        for(int i=0;i<allEmployees.getEmployees().size();i++)
        {
            employeesList.add(allEmployees.getEmployees().get(i).getName());
            hm.put(allEmployees.getEmployees().get(i).getName(),allEmployees.getEmployees().get(i).getEmpId());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employeesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addEmployeeSpinner.setAdapter(adapter);
        addEmployeeSpinner.setPrompt("Select Employee");
        addEmployeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    Toast.makeText(getApplicationContext(), "Adding Employee ...", Toast.LENGTH_SHORT).show();

                    AddEmployeesUnderManagerModel object = new AddEmployeesUnderManagerModel();
                    object.setEmpId(String.valueOf(hm.get(employeesList.get(i))));
                    object.setManagerId(b.getString("manager_id"));


                    AddEmployeeUnderManager add = ServiceGenerator.createService(AddEmployeeUnderManager.class, DbHandler.getString(getApplicationContext(), "bearer", ""));
                    Call<AddEmployeesUnderManagerResponse> addResponse = add.responseAdded(object);
                    addResponse.enqueue(new Callback<AddEmployeesUnderManagerResponse>() {
                        @Override
                        public void onResponse(Call<AddEmployeesUnderManagerResponse> call, Response<AddEmployeesUnderManagerResponse> response) {
                            AddEmployeesUnderManagerResponse employeesUnderManagerResponse = response.body();
                            if (response.code() == 200) {
                                Toast.makeText(getApplicationContext(), employeesUnderManagerResponse.getMsg(), Toast.LENGTH_LONG).show();
                                refreshData(man_id);
                                //Toast.makeText(getApplicationContext(),"Refresh to see changes",Toast.LENGTH_LONG).show();
                            } else if (response.code() == 403) {
                                DbHandler.unsetSession(getApplicationContext(), "isForcedLoggedOut");
                            }
                        }

                        @Override
                        public void onFailure(Call<AddEmployeesUnderManagerResponse> call, Throwable t) {
                            handleNetworkErrors(t, 1);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void refreshData(String man_id)
    {
        EmployeesUnderManagerModel object=new EmployeesUnderManagerModel();
        object.setEmp_id(man_id);
        EmployeesUnderManager employeesUnderManager= ServiceGenerator.createService(EmployeesUnderManager.class, DbHandler.getString(getApplicationContext(),"bearer",""));
        Call<EmployeesUnderManagerResponse> call=employeesUnderManager.employees(object);
        call.enqueue(new Callback<EmployeesUnderManagerResponse>() {
            @Override
            public void onResponse(Call<EmployeesUnderManagerResponse> call, Response<EmployeesUnderManagerResponse> response) {
                managerResponse=response.body();
                swipeRefresh.setRefreshing(false);
                if(response.code()==200)
                {
                    if(managerResponse.getSuccess()){
                        existingEmployees.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        existingEmployees.setAdapter(new EmployeesUnderManagerAdapter(getApplicationContext(),managerResponse,man_id));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Some Error Occured",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(response.code()==403)
                {
                    DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<EmployeesUnderManagerResponse> call, Throwable t) {

            }
        });

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
