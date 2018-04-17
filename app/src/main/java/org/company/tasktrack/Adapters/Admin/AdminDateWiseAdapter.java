package org.company.tasktrack.Adapters.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 8/4/18.
 */

public class AdminDateWiseAdapter extends RecyclerView.Adapter<AdminDateWiseAdapter.viewHolder> {

    Context context;
    GetAssignedTaskResponse response;
    public AdminDateWiseAdapter(Context context,GetAssignedTaskResponse response)
    {
        this.response=response;
        this.context=context;
    }

    @Override
    public AdminDateWiseAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_date_wise_report_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminDateWiseAdapter.viewHolder holder, int position) {
        holder.taskTitle.setText(response.getTasks().get(position).getName());
        holder.date.setText("Assigned On -"+response.getTasks().get(position).getAssignDate());
        holder.status.setText(response.getTasks().get(position).getTaskStatus());
        holder.assignedBy.setText("Assigned By -"+response.getTasks().get(position).getEmpDetails().get(0).getName()+" ("+response.getTasks().get(position).getEmpDetails().get(0).getEmpId()+")");
        if(response.getTasks().get(position).getComplete_date().equals("")){
            holder.completedOn.setVisibility(View.GONE);
        }
        else{
            holder.completedOn.setText("Completed On-"+response.getTasks().get(position).getComplete_date());
        }
        if(response.getTasks().get(position).getTaskStatus().equals("PENDING"))
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        else{
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.green_dark));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spanned message;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    message= Html.fromHtml("<ul><li><b>Description</b><br><p>"+response.getTasks().get(position).getDesc()+"<p></li><br><li><b>Manager Remark</b><br><p>"+response.getTasks().get(position).getRemarkManager()+"</p></li><br><li><b>Employee Remark</b><br><p>"+response.getTasks().get(position).getRemark_employee()+"</p></li></ul>", Html.FROM_HTML_MODE_COMPACT);
                }
                else{
                    message=Html.fromHtml("<ul><li><b>Description</b><br><p>"+response.getTasks().get(position).getDesc()+"<p></li><br><li><b>Manager Remark</b><br><p>"+response.getTasks().get(position).getRemarkManager()+"</p></li><br><li><b>Employee Remark</b><br><p>"+response.getTasks().get(position).getRemark_employee()+"</p></li></ul>");
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
        try {
            return response.getTasks().size();
        }catch (Exception e){
            return 0;
        }
    }
    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.statusCard)
        CardView statusCard;
        @BindView(R.id.taskTitle)
        TextView taskTitle;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.assignedBy)
        TextView assignedBy;
        @BindView(R.id.completedOn)
        TextView completedOn;
        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
