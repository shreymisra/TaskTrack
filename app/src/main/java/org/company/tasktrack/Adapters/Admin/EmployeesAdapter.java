package org.company.tasktrack.Adapters.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.company.tasktrack.Networking.Models.DeleteUserModel;
import org.company.tasktrack.Networking.Models.DeleteUserResponse;
import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.Networking.Models.UpdatePasswordModel;
import org.company.tasktrack.Networking.Models.UpdatePasswordResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.DeleteUserService;
import org.company.tasktrack.Networking.Services.UpdatePassword;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shrey on 8/4/18.
 */

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.viewHolder> {

    Context context;
    GetAllEmployeesResponse response;
    ProgressDialog progressDialog;
    public EmployeesAdapter(Context context,GetAllEmployeesResponse response)
    {
        this.context=context;
        this.response=response;
        progressDialog=new ProgressDialog(context);
    }
    @Override
    public EmployeesAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeesAdapter.viewHolder holder, int position) {
        holder.sno.setText(String.valueOf(position+1));
        holder.name.setText(response.getEmployees().get(position).getName()+" ("+response.getEmployees().get(position).getEmpId()+")");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to delete this user ?")
                        .setTitle("Confirm")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DeleteUserModel object=new DeleteUserModel();
                                object.setEmpId(String.valueOf(response.getEmployees().get(position).getEmpId()));
                                DeleteUserService deleteUserService= ServiceGenerator.createService(DeleteUserService.class, DbHandler.getString(context,"bearer",""));
                                Call<DeleteUserResponse> call=deleteUserService.deleteResponse(object);
                                call.enqueue(new Callback<DeleteUserResponse>() {
                                    @Override
                                    public void onResponse(Call<DeleteUserResponse> call, Response<DeleteUserResponse> responseP) {
                                        DeleteUserResponse deleteUserResponse=responseP.body();
                                        if(responseP.code()==200){
                                            if(deleteUserResponse.getSuccess()){
                                                response.getEmployees().remove(position);
                                                notifyDataSetChanged();
                                            }
                                            Toast.makeText(context,deleteUserResponse.getMsg(),Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            DbHandler.unsetSession(context,"isForcedLoggedOut");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DeleteUserResponse> call, Throwable t) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile(response.getEmployees().get(position).getName(),response.getEmployees().get(position).getEmpId());
            }
        });
    }

    public void editProfile(String empName,int emp_id) {
          View dialogView = LayoutInflater.from(context).inflate(R.layout.profile_update_admin, null);

         EditText email = (EditText) dialogView.findViewById(R.id.email);
         EditText phone=(EditText)dialogView.findViewById(R.id.phone);
         EditText pass=(EditText)dialogView.findViewById(R.id.yourPass);
        TextInputLayout emailLayout=(TextInputLayout)dialogView.findViewById(R.id.emailLayout);
        TextInputLayout phoneLayout=(TextInputLayout)dialogView.findViewById(R.id.phoneLayout);
        TextInputLayout passLayout=(TextInputLayout)dialogView.findViewById(R.id.yourPassLayout);
        passLayout.setVisibility(View.GONE);


        TextView resetPass=(TextView)dialogView.findViewById(R.id.resetPassword);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    emailLayout.setVisibility(View.GONE);
                    phoneLayout.setVisibility(View.GONE);
                    passLayout.setVisibility(View.VISIBLE);
                    resetPass.setVisibility(View.GONE);

                   // text.setText("Reset Profile Details");

            }
        });


         new AlertDialog.Builder(context)
                 .setView(dialogView)
                 .setTitle(empName.toUpperCase())
                 .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {

                        if(passLayout.getVisibility()==View.VISIBLE){
                            if(!pass.getText().toString().equals("")) {
                                UpdatePasswordModel object = new UpdatePasswordModel();
                                object.setNewPass("");
                                object.setOldPass(pass.getText().toString());
                                object.setEmp_id(String.valueOf(emp_id));

                                progressDialog.setTitle("Please Wait");
                                progressDialog.setMessage("Reseting the employee's password ...");
                                progressDialog.show();

                                UpdatePassword updatePassword=ServiceGenerator.createService(UpdatePassword.class,DbHandler.getString(context,"bearer",""));
                                Call<UpdatePasswordResponse> call=updatePassword.responseUpdatePassword(object);
                                call.enqueue(new Callback<UpdatePasswordResponse>() {
                                    @Override
                                    public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                                        progressDialog.dismiss();
                                        UpdatePasswordResponse passwordResponse=response.body();
                                        if(response.code()==200)
                                        {
                                            Toast.makeText(context,passwordResponse.getMsg(),Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                        else if(response.code()==403){
                                            DbHandler.unsetSession(context,"isForcedLoggedOut");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }else{
                                Toast.makeText(context,"Password cannot be left blank",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(emailLayout.getVisibility()==View.VISIBLE && phoneLayout.getVisibility()==View.VISIBLE)
                        {

                        }
                     }
                 })
                 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         }).create().show();

        /*dialogBuilder.setTitle(empName.toUpperCase())
                    .setMessage("Update Your Details")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();*/
    }
    @Override
    public int getItemCount() {
        return response.getEmployees().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.sno)
        TextView sno;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.edit)
        ImageView edit;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
