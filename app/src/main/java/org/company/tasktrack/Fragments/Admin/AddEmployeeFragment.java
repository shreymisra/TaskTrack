package org.company.tasktrack.Fragments.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Adapters.Admin.EmployeesAdapter;
import org.company.tasktrack.Adapters.Admin.ManagersAdapter;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.Networking.Models.AddUserModel;
import org.company.tasktrack.Networking.Models.AddUserResponse;
import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.Networking.Models.GetAllManagersResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.AddUserService;
import org.company.tasktrack.Networking.Services.GetAllEmployees;
import org.company.tasktrack.Networking.Services.GetAllManagers;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddEmployeeFragment extends BaseFragment {
    View view;

    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.type)
    RadioGroup type;
    @BindView(R.id.manager)
    RadioButton manager;
    @BindView(R.id.employee)
    RadioButton employee;
    @BindView(R.id.addUser)
    Button addUser;
    @BindView(R.id.passLayout)
    TextInputLayout passLayout;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.l1)
    LinearLayout l1;

    ProgressDialog progressDialog;
    AddUserModel addUserModel;
    Gson gson;
  public static AddEmployeeFragment newInstance(String param1, String param2) {
        AddEmployeeFragment fragment = new AddEmployeeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_add_employee, container, false);
        ButterKnife.bind(this,view);
        clearData();
        progressDialog=new ProgressDialog(getContext());
        addUserModel=new AddUserModel();
        gson=new Gson();
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.manager:addUserModel.setType("Manager");
                    break;
                    case R.id.employee:addUserModel.setType("Employee");
                    break;
                }
            }
        });
        return view;
    }

    @OnClick(R.id.addUser)
    public void addUser()
    {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(l1.getWindowToken(), 0);

        int flag=checkDetails();

        if(flag==1)
        {
            addUserModel.setName(name.getText().toString());
            addUserModel.setMobile(phoneNumber.getText().toString());
            addUserModel.setEmail(email.getText().toString());
            addUserModel.setPassword(password.getText().toString());

            progressDialog.setMessage("Adding User ...");
            progressDialog.setTitle("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            AddUserService addUserService= ServiceGenerator.createService(AddUserService.class, DbHandler.getString(getContext(),"bearer","false"));
            Call<AddUserResponse> call=addUserService.addUser(addUserModel);
            call.enqueue(new Callback<AddUserResponse>() {
                @Override
                public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {
                    AddUserResponse addUserResponse=response.body();
                    progressDialog.dismiss();
                    if(response.code()==200)
                    {
                        clearData();
                        Toast.makeText(getContext(), addUserResponse.getMsg(), Toast.LENGTH_LONG).show();
                        if(addUserResponse.getSuccess()) {
                                    if(addUserModel.getType().equals("Manager"))
                                        fetchManagers();
                                    else
                                        fetchEmployees();
                                }
                                else{
                            Toast.makeText(getContext(),"Some Error Occurred",Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    }else{
                        DbHandler.unsetSession(getContext(),"isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<AddUserResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    handleNetworkErrors(t,1);
                }
            });
        }
    }

    public int checkDetails()
    {
        int flag=0;

        if(!name.getText().toString().equals(""))
            flag=flag+1;
        else{
            nameLayout.setError("Field Required");
            flag=0;
        }

        if(!phoneNumber.getText().toString().equals(""))
        {
            if(!Pattern.matches("[a-zA-Z]+", phoneNumber.getText().toString())) {
                if(phoneNumber.getText().toString().length() != 10) {
                    phoneLayout.setError("Please Enter a Valid Phone Number");
                    flag=0;
                } else {
                    flag=flag+1;
                  }
            } else {
                phoneLayout.setError("Please Enter a Valid Phone Number");
                flag=0;
              }
        }
        else
        {
            phoneLayout.setError("Field Required");
            flag=0;
        }

        if(!email.getText().toString().equals(""))
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                flag=flag+1;
             }else{
                emailLayout.setError("Please Enter a Valid E-Mail Address");
                flag=0;
               }
        }
        else
        {
            emailLayout.setError("Field Required");
            flag=0;
        }

        if(!password.getText().toString().equals(""))
            flag=flag+1;
        else
        {
            passLayout.setError("Field Required");
            flag=0;
        }

        if(type.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(getContext(),"Select Type",Toast.LENGTH_SHORT).show();
            flag=0;
        }
        else
        {
            flag=flag+1;
        }
       // Toast.makeText(getContext(),String.valueOf(type.getCheckedRadioButtonId()),Toast.LENGTH_SHORT).show();


        if(flag==5)
            return 1;
        else
            return 0;

    }

    public void clearData()
    {
        name.setText("");
        phoneNumber.setText("");
        email.setText("");
        password.setText("");
        nameLayout.setErrorEnabled(false);
        phoneLayout.setErrorEnabled(false);
        passLayout.setErrorEnabled(false);
        emailLayout.setErrorEnabled(false);
        type.clearCheck();
    }

    public void fetchEmployees(){
            GetAllEmployees employees = ServiceGenerator.createService(GetAllEmployees.class, DbHandler.getString(getContext(), "bearer", ""));
            Call<GetAllEmployeesResponse> employeesResponse = employees.getAllEmployees();
            employeesResponse.enqueue(new Callback<GetAllEmployeesResponse>() {
                @Override
                public void onResponse(Call<GetAllEmployeesResponse> call, Response<GetAllEmployeesResponse> response) {

                    if (response.code() == 200) {
                        GetAllEmployeesResponse allEmployees = response.body();
                        if (allEmployees.getSuccess()) {
                            DbHandler.remove(getContext(),"Employees");
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

    public void fetchManagers(){
        GetAllManagers managers = ServiceGenerator.createService(GetAllManagers.class, DbHandler.getString(getContext(), "bearer", ""));
        Call<GetAllManagersResponse> managersResponse = managers.getAllManagers();
        managersResponse.enqueue(new Callback<GetAllManagersResponse>() {
            @Override
            public void onResponse(Call<GetAllManagersResponse> call, Response<GetAllManagersResponse> response) {

                if (response.code() == 200) {
                    GetAllManagersResponse allManagers = response.body();

                    if (allManagers.getSuccess()) {
                        DbHandler.remove(getContext(),"Managers");
                        DbHandler.putString(getContext(), "Managers", gson.toJson(allManagers));
                    }
                    else
                        Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_LONG).show();

                } else if (response.code() == 403) {
                    DbHandler.unsetSession(getContext(), "isForcedLoggedOut");
                }
            }

            @Override
            public void onFailure(Call<GetAllManagersResponse> call, Throwable t) {
                handleNetworkErrors(t, -1);
            }
        });
    }

/*
    @OnTextChanged(R.id.name)
    public void setName(){
        if(name.getText().toString().equals("")){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError("Please Enter Your Name");
        }
        else{
            nameLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.password)
    public void setPassword(){
        if(password.getText().toString().equals("")){
            passLayout.setErrorEnabled(true);
            passLayout.setError("Please Enter Password");
        }
        else{
            passLayout.setErrorEnabled(false);
        }
    }
    @OnTextChanged(R.id.email)
    public void setEmail(){
        if(email.getText().toString().equals("")){
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Please Enter E-Mail");
        }
        else{
            emailLayout.setErrorEnabled(false);
        }
    }

    @OnTextChanged(R.id.phoneNumber)
    public  void setPhone(){
        if(phoneNumber.getText().toString().equals("")){
            phoneLayout.setErrorEnabled(true);
            phoneLayout.setError("Please Enter Phone Number");
        }
        else{
            phoneLayout.setErrorEnabled(false);
        }
    }

    @OnCheckedChanged(R.id.type)
    public void setType(){
        Toast.makeText(getContext(),String.valueOf(type.getCheckedRadioButtonId()),Toast.LENGTH_SHORT).show();
    }*/
}
