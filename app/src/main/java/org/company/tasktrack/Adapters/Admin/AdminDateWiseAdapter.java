package org.company.tasktrack.Adapters.Admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 8/4/18.
 */

public class AdminDateWiseAdapter extends RecyclerView.Adapter<AdminDateWiseAdapter.viewHolder> {

    Context context;
    public AdminDateWiseAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public AdminDateWiseAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_date_wise_report_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminDateWiseAdapter.viewHolder holder, int position) {
        holder.taskTitle.setText("Task Title");
        holder.date.setText("21-05-2018");
        holder.desc.setText("Description");
        holder.status.setText("Completed");
    }

    @Override
    public int getItemCount() {
        return 10;
    }
    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.taskTitle)
        TextView taskTitle;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.description)
        TextView desc;
        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
