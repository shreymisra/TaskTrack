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

import org.company.tasktrack.Networking.Models.GetAllEmployeesResponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 8/4/18.
 */

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.viewHolder> {

    Context context;
    GetAllEmployeesResponse response;
    public EmployeesAdapter(Context context,GetAllEmployeesResponse response)
    {
        this.context=context;
        this.response=response;
    }
    @Override
    public EmployeesAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeesAdapter.viewHolder holder, int position) {
        holder.sno.setText(String.valueOf(position+1));
        holder.name.setText(response.getEmployees().get(position).getName()+" ("+response.getEmployees().get(position).getEmpId()+")");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to delete this user ?")
                        .setTitle("Confirm")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               holder.itemView.setVisibility(View.GONE);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        return response.getEmployees().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.sno)
        TextView sno;
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
