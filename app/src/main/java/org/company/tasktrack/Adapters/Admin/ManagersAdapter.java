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

import org.company.tasktrack.Networking.Models.GetAllManagersResponse;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 8/4/18.
 */

public class ManagersAdapter extends RecyclerView.Adapter<ManagersAdapter.viewHolder> {
    Context context;
    GetAllManagersResponse response;
    public ManagersAdapter(Context context, GetAllManagersResponse response) {
        this.context=context;
        this.response=response;

    }

    @Override
    public ManagersAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManagersAdapter.viewHolder holder, int position) {
        holder.sno.setText(String.valueOf(position+1));
        holder.name.setText(response.getManagers().get(position).getName()+" ("+response.getManagers().get(position).getEmpId()+")");
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // context.startActivity(ManagerEmployeeActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.getManagers().size();
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
