package org.company.tasktrack.Adapters.Manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.company.tasktrack.Adapters.Employee.EmployeeTaskImagesAdapter;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 11/4/18.
 */

public class ManagerAssignedTasksAdapter extends RecyclerView.Adapter<ManagerAssignedTasksAdapter.viewHolder> {

    Context context;
    public ManagerAssignedTasksAdapter(Context context){
        this.context=context;
    }
    @Override
    public ManagerAssignedTasksAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_assigned_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManagerAssignedTasksAdapter.viewHolder holder, int position) {
        holder.name.setText("Shrey Misra");
        holder.taskTitle.setText("Task Assigned");
        holder.taskDescription.setText("Do as directed");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.taskTitle)
        TextView taskTitle;
        @BindView(R.id.taskDescription)
        TextView taskDescription;
        @BindView(R.id.status)
        ImageView status;
        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
