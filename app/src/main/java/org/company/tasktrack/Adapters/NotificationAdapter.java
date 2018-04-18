package org.company.tasktrack.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.company.tasktrack.Adapters.Manager.ManagerAssignedTasksAdapter;
import org.company.tasktrack.Networking.Models.NotificationModel;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.ImageTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 16/4/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    Context context;
    Gson gson;
    List<NotificationModel> response;
    public NotificationAdapter(Context context, List<NotificationModel> response){
        this.response=response;
        this.context=context;
        gson=new Gson();
    }
    @Override
    public NotificationAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.viewHolder holder, int position) {
        holder.date.setText(response.get(position).getDate());
        holder.person.setText(response.get(position).getPerson());
        holder.desc.setText(response.get(position).getMessage());
        holder.title.setText(response.get(position).getTitle());
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response.remove(position);
                DbHandler.remove(context,"notificationList");
                DbHandler.putString(context, "notificationList", gson.toJson(response));
                notifyDataSetChanged();
            }
        });
        if(!response.get(position).getImageUrl().equals(""))
        Picasso.with(context)
                .load(response.get(position).getImageUrl())
                .transform(new ImageTransform())
                .placeholder(R.drawable.ic_notifications_active_black_24dp)
                .error(R.drawable.ic_notifications_active_black_24dp)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return response.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.person)
        TextView person;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.close)
        ImageView close;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
