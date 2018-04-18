package org.company.tasktrack.Adapters.Admin;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.company.tasktrack.Networking.Models.DayWiseReportDatum;
import org.company.tasktrack.Networking.Models.DayWiseReportTasks;
import org.company.tasktrack.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 8/4/18.
 */

public class AdminDayWiseTaskListAdapter extends RecyclerView.Adapter<AdminDayWiseTaskListAdapter.viewHolder> {

    Context context;
    List<DayWiseReportTasks> response;
    public AdminDayWiseTaskListAdapter(Context context,List<DayWiseReportTasks> response)
    {
        this.response=response;
        this.context=context;
    }
    @Override
    public AdminDayWiseTaskListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_day_wise_task_list, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminDayWiseTaskListAdapter.viewHolder holder, int position) {
    holder.taskname.setText(response.get(position).getTask());
   // holder.taskname.setTextColor(Color.parseColor(response.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.taskName)
        TextView taskname;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
