package org.company.tasktrack.Adapters.Employee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shrey on 3/4/18.
 */

public class EmployeeTaskImagesAdapter extends RecyclerView.Adapter<EmployeeTaskImagesAdapter.viewHolder> {

    Context context;
    public EmployeeTaskImagesAdapter(Context context){
        this.context=context;
    }

    @Override
    public EmployeeTaskImagesAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_image, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeTaskImagesAdapter.viewHolder holder, int position) {
    holder.taskImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.taskImage)
        ImageView taskImage;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
