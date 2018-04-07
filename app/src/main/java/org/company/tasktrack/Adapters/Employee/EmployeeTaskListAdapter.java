package org.company.tasktrack.Adapters.Employee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.company.tasktrack.Activities.EmployeeTaskActivity;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 3/4/18.
 */

public class EmployeeTaskListAdapter extends RecyclerView.Adapter<EmployeeTaskListAdapter.viewHolder> {
   Context context;
   int i=1;
   public EmployeeTaskListAdapter(Context context){
      this.context=context;
   }

    @Override
    public EmployeeTaskListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeTaskListAdapter.viewHolder holder, int position) {
       holder.sno.setText(String.valueOf(i));
       i++;
        holder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               context.startActivity(new Intent(context,EmployeeTaskActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.sno)
        TextView sno;
        @BindView(R.id.taskTitle)
        TextView taskTitle;
        @BindView(R.id.taskDesc)
        TextView taskDesc;
        @BindView(R.id.taskPriority)
        TextView taskPriority;
        @BindView(R.id.taskCard)
        CardView taskCard;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}