package org.company.tasktrack.Fragments.Admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.R;


public class AddEmployeeFragment extends Fragment {
    View view;


  public static AddEmployeeFragment newInstance(String param1, String param2) {
        AddEmployeeFragment fragment = new AddEmployeeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_add_employee, container, false);
        return view;
    }

}
