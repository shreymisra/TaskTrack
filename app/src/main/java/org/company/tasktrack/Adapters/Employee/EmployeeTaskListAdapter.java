package org.company.tasktrack.Adapters.Employee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.company.tasktrack.Activities.EmployeeTaskActivity;
import org.company.tasktrack.Networking.Models.GetAssignedTaskResponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 3/4/18.
 */

public class EmployeeTaskListAdapter extends RecyclerView.Adapter<EmployeeTaskListAdapter.viewHolder> {
   Context context;
   int i=1;
   GetAssignedTaskResponse response;
   Gson gson;
   public EmployeeTaskListAdapter(Context context,GetAssignedTaskResponse response){
      this.context=context;
      this.response=response;
       gson=new Gson();
   }

    @Override
    public EmployeeTaskListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeTaskListAdapter.viewHolder holder, int position) {
       holder.sno.setText(String.valueOf(position+1));
        holder.date.setText("Assigned On - "+response.getTasks().get(position).getAssignDate());
        holder.taskTitle.setText(response.getTasks().get(position).getName());
        holder.assignedBy.setText("Assigned By - "+response.getTasks().get(position).getEmpDetails().get(0).getName()+" ("+response.getTasks().get(position).getEmpDetails().get(0).getEmpId()+")");

        if(response.getTasks().get(position).getPriority()==0)
        {
            holder.taskPriority.setText("Low Priority");
            holder.priorityCard.setBackgroundColor(context.getResources().getColor(R.color.green_dark));
           // holder.priorityCard.setRadius(8);
        }
        else if(response.getTasks().get(position).getPriority()==1)
        {
            holder.taskPriority.setText("Average Priority");
            holder.priorityCard.setBackgroundColor(context.getResources().getColor(R.color.black));
            //holder.priorityCard.setRadius(6);
        }
        else if(response.getTasks().get(position).getPriority()==2)
        {
            holder.taskPriority.setText("High Priority");
            holder.priorityCard.setBackgroundColor(context.getResources().getColor(R.color.red));
           // holder.priorityCard.setRadius(4);
        }


        holder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,EmployeeTaskActivity.class);
                Bundle b=new Bundle();
                b.putString("TaskDetails",gson.toJson(response.getTasks().get(position)));
                intent.putExtras(b);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.getTasks().size();
    }
    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.sno)
        TextView sno;
        @BindView(R.id.taskTitle)
        TextView taskTitle;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.assignedBy)
        TextView assignedBy;
        @BindView(R.id.taskPriority)
        TextView taskPriority;
        @BindView(R.id.taskCard)
        CardView taskCard;
        @BindView(R.id.priorityCard)
        CardView priorityCard;
        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
