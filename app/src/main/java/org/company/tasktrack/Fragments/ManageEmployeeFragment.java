package org.company.tasktrack.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.R;

public class ManageEmployeeFragment extends Fragment {

    View view;
    public static ManageEmployeeFragment newInstance(String param1, String param2) {
        ManageEmployeeFragment fragment = new ManageEmployeeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_manage_employee, container, false);
        return view;
    }

}
