package org.company.tasktrack.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.R;

/**
 * Created by shrey on 3/4/18.
 */

public class EmployeeTaskImagesAdapter extends RecyclerView.Adapter<EmployeeTaskImagesAdapter.viewHolder> {
    @Override
    public EmployeeTaskImagesAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_image, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeTaskImagesAdapter.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
    public class viewHolder extends RecyclerView.ViewHolder{

        public viewHolder(View itemView) {
            super(itemView);
        }
    }
}
