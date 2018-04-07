package org.company.tasktrack.Fragments.Admin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.company.tasktrack.Adapters.Admin.ViewPagerAdapter;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.NonSwipeableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageFragment extends Fragment {

    View view;

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewPager;
    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_manage, container, false);
        ButterKnife.bind(this,view);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(new Manage_Manager(), "Managers");
            adapter.addFragment(new Manage_Employee(), "Employees");
        viewPager.setAdapter(adapter);
    }
}
