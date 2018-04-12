package org.company.tasktrack.Adapters.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.company.tasktrack.Networking.Models.DeleteEmployeeUnderManagerModel;
import org.company.tasktrack.Networking.Models.DeleteEmployeeUnderManagerResponse;
import org.company.tasktrack.Networking.Models.EmployeesUnderManagerResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.DeleteEmployeeUnderManager;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shrey on 13/4/18.
 */

public class EmployeesUnderManagerAdapter extends RecyclerView.Adapter<EmployeesUnderManagerAdapter.viewHolder> {

    Context context;
    EmployeesUnderManagerResponse response;
    String man_id;
    public EmployeesUnderManagerAdapter(Context context, EmployeesUnderManagerResponse response,String man_id){
        this.context=context;
        this.response=response;
        this.man_id=man_id;
    }

    @Override
    public EmployeesUnderManagerAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_under_manager, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeesUnderManagerAdapter.viewHolder holder, int position) {
            holder.name.setText(response.getEmployees().get(position).getName());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Deleting ... ",Toast.LENGTH_SHORT).show();
                    DeleteEmployeeUnderManagerModel object=new DeleteEmployeeUnderManagerModel();
                    object.setEmpId(String.valueOf(response.getEmployees().get(position).getEmpId()));
                    object.setManagerId(man_id);
                    DeleteEmployeeUnderManager delete= ServiceGenerator.createService(DeleteEmployeeUnderManager.class, DbHandler.getString(context,"bearer",""));
                    Call<DeleteEmployeeUnderManagerResponse> deleteResponse=delete.responseDelete(object);
                    deleteResponse.enqueue(new Callback<DeleteEmployeeUnderManagerResponse>() {
                        @Override
                        public void onResponse(Call<DeleteEmployeeUnderManagerResponse> call, Response<DeleteEmployeeUnderManagerResponse> responseP) {
                            if(responseP.code()==200){
                                DeleteEmployeeUnderManagerResponse r=responseP.body();
                                if(r.getSuccess()){
                                    response.getEmployees().remove(position);
                                    notifyDataSetChanged();
                                }
                                Toast.makeText(context,r.getMsg(),Toast.LENGTH_LONG).show();
                            }
                            else{
                                DbHandler.unsetSession(context,"isForceLoggedOut");
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteEmployeeUnderManagerResponse> call, Throwable t) {

                        }
                    });
                    /*new AlertDialog.Builder(context)
                            .setTitle("Confirm")
                            .setMessage("Are you sure you want to delete this employee ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();*/
                }
            });
    }

    @Override
    public int getItemCount() {
        return response.getEmployees().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.delete)
        ImageView delete;
        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
