package org.company.tasktrack.Fragments.Manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.R;

import butterknife.ButterKnife;

public class ManagerReportFragment extends Fragment {

    public static ManagerReportFragment newInstance(String param1, String param2) {
        ManagerReportFragment fragment = new ManagerReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_manager_report, container, false);
        ButterKnife.bind(this,view);

        return view;
    }
}
