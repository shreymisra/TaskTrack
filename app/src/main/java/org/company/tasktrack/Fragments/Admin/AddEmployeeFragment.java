package org.company.tasktrack.Fragments.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.company.tasktrack.Fragments.BaseFragment;
import org.company.tasktrack.Networking.Models.AddUserModel;
import org.company.tasktrack.Networking.Models.AddUserResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.AddUserService;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


    ProgressDialog progressDialog;
    AddUserModel addUserModel;
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
        progressDialog=new ProgressDialog(getContext());
        addUserModel=new AddUserModel();
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
                    if(response.code()==200)
                    {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Message")
                                    .setMessage(addUserResponse.getMsg())
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            clearData();
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
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
            flag=flag+1;
        else
        {
            phoneLayout.setError("Field Required");
            flag=0;
        }

        if(!email.getText().toString().equals(""))
            flag=flag+1;
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

        if(type.getCheckedRadioButtonId()==0)
        {
            Toast.makeText(getContext(),"Select Type",Toast.LENGTH_SHORT).show();
            flag=0;
        }
        else
            flag=flag+1;


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
    }

}
