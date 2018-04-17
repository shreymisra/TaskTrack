package org.company.tasktrack.Adapters.Manager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Adapters.Employee.EmployeeTaskImagesAdapter;
import org.company.tasktrack.Networking.Models.DeleteTaskModel;
import org.company.tasktrack.Networking.Models.DeleteTaskResponse;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.DeleteTask;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shrey on 11/4/18.
 */

public class ManagerAssignedTasksAdapter extends RecyclerView.Adapter<ManagerAssignedTasksAdapter.viewHolder> {

    Context context;
    GetAssignedTaskResponse  response;
    Gson gson=new Gson();
    /*SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2=new SimpleDateFormat("dd-MM-yyyy");
    */public ManagerAssignedTasksAdapter(Context context,GetAssignedTaskResponse response){
        this.context=context;
        this.response=response;
    }
    @Override
    public ManagerAssignedTasksAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_assigned_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManagerAssignedTasksAdapter.viewHolder holder, int position) {
        holder.name.setText(response.getTasks().get(position).getEmpDetails().get(0).getName()+" ("+response.getTasks().get(position).getEmpDetails().get(0).getEmpId()+")");
        holder.taskTitle.setText(response.getTasks().get(position).getName());

       if(response.getTasks().get(position).getComplete_date().equals("")){
           holder.completeDate.setVisibility(View.GONE);
       }
       else{
           holder.completeDate.setVisibility(View.VISIBLE);
           holder.completeDate.setText("Completed On - "+response.getTasks().get(position).getComplete_date());
       }
       /* Date date= null;
        try {
            date = sdf1.parse(response.getTasks().get(position).getAssignDate().substring(0,10));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.date.setText("Assigned On - "+response.getTasks().get(position).getAssignDate());

        if(response.getTasks().get(position).getPriority()==0)
        {
            holder.priority.setText("Low");
            holder.priorityCard.setBackgroundColor(context.getResources().getColor(R.color.green_dark));
            holder.priorityCard.setRadius(8);
        }
        else if(response.getTasks().get(position).getPriority()==1)
        {
            holder.priority.setText("Average");
            holder.priorityCard.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.priorityCard.setRadius(6);
        }
        else if(response.getTasks().get(position).getPriority()==2)
        {
            holder.priority.setText("High");
            holder.priorityCard.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.priorityCard.setRadius(4);
        }

        if(response.getTasks().get(position).getTaskStatus().equals("PENDING"))
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pending_24dp));
        else
        {
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_completed_24dp));
            holder.delete.setVisibility(View.INVISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm")
                        .setMessage("Do you really want to delete this task ?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DeleteTaskModel object=new DeleteTaskModel();
                                object.setTaskId(response.getTasks().get(position).getId());
                                DeleteTask deleteTask= ServiceGenerator.createService(DeleteTask.class, DbHandler.getString(context,"bearer",""));
                                Call<DeleteTaskResponse> call=deleteTask.responseDelete(object);
                                call.enqueue(new Callback<DeleteTaskResponse>() {
                                    @Override
                                    public void onResponse(Call<DeleteTaskResponse> call, Response<DeleteTaskResponse> responseP) {
                                        if(responseP.code()==200){
                                            DeleteTaskResponse deleteTaskResponse=responseP.body();
                                            if(deleteTaskResponse.getSuccess()){
                                                response.getTasks().remove(position);
                                                Toast.makeText(context,deleteTaskResponse.getMsg(),Toast.LENGTH_SHORT).show();
                                                DbHandler.remove(context,"AssignedTasks");
                                                DbHandler.putString(context,"AssignedTasks",gson.toJson(response));
                                                notifyDataSetChanged();
                                            }else{
                                                Toast.makeText(context,deleteTaskResponse.getMsg(),Toast.LENGTH_LONG).show();
                                            }
                                        }else{
                                            DbHandler.unsetSession(context,"isForcedLoggedOut");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DeleteTaskResponse> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            }
                        }).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spanned message;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    //message=Html.fromHtml("<ul><li><b>Description</b><br><p>"+response.getTasks().get(position).getDesc()+"<p></li><br><li><b>Remark</b><br><p>"+response.getTasks().get(position).getRemarkManager()+"</p></li></ul>", Html.FROM_HTML_MODE_COMPACT);
                    message= Html.fromHtml("<ul><li><b>Description</b><br><p>"+response.getTasks().get(position).getDesc()+"<p></li><br><li><b>Manager Remark</b><br><p>"+response.getTasks().get(position).getRemarkManager()+"</p></li><br><li><b>Employee Remark</b><br><p>"+response.getTasks().get(position).getRemark_employee()+"</p></li></ul>", Html.FROM_HTML_MODE_COMPACT);
                }
                else{
                   // message=Html.fromHtml("<ul><li><b>Description</b><br><p>"+response.getTasks().get(position).getDesc()+"<p></li><br><li><b>Remark</b><br><p>"+response.getTasks().get(position).getRemarkManager()+"</p></li></ul>");
                    message= Html.fromHtml("<ul><li><b>Description</b><br><p>"+response.getTasks().get(position).getDesc()+"<p></li><br><li><b>Manager Remark</b><br><p>"+response.getTasks().get(position).getRemarkManager()+"</p></li><br><li><b>Employee Remark</b><br><p>"+response.getTasks().get(position).getRemark_employee()+"</p></li></ul>");
                }
                new AlertDialog.Builder(context)
                        .setTitle(response.getTasks().get(position).getName())
                        .setMessage(message)
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return response.getTasks().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.priorityCard)
        CardView priorityCard;
        @BindView(R.id.priority)
        TextView priority;
        @BindView(R.id.taskTitle)
        TextView taskTitle;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.status)
        ImageView status;
        @BindView(R.id.completeDate)
        TextView completeDate;
        public viewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
