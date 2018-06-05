package org.company.tasktrack.Adapters.Admin;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.company.tasktrack.Networking.Models.DayWiseReportReponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 8/4/18.
 */

public class AdminDayWiseAdapter extends RecyclerView.Adapter<AdminDayWiseAdapter.viewHolder> {

    Context context;
    DayWiseReportReponse response;
    public AdminDayWiseAdapter(Context context, DayWiseReportReponse response)
    {
        this.response=response;
        this.context=context;
    }
    @Override
    public AdminDayWiseAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_day_wise_report_task, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminDayWiseAdapter.viewHolder holder, int position) {
        holder.time.setText(response.getReport().get(position).getTimeStart()+" - "+response.getReport().get(position).getTimeEnd());
        holder.lists.setLayoutManager(new LinearLayoutManager(context));
        holder.lists.setAdapter(new AdminDayWiseTaskListAdapter(context,response.getReport().get(position).getTasks()));
        if(response.getReport().get(position).getRed_count().equals("1"))
            holder.remark_status.setBackground(context.getResources().getDrawable(R.drawable.remark_no));
        else
            holder.remark_status.setBackground(context.getResources().getDrawable(R.drawable.remark_yes));
    }

    @Override
    public int getItemCount() {
        return response.getReport().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.line)
        View line;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.lists)
        RecyclerView lists;
        @BindView(R.id.remark_status)
        View remark_status;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
