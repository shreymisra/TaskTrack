package org.company.tasktrack.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 3/4/18.
 */

public class EmployeeTaskListAdapter extends RecyclerView.Adapter<EmployeeTaskListAdapter.viewHolder> {
    @Override
    public EmployeeTaskListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeTaskListAdapter.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
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

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
