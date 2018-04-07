package org.company.tasktrack.Activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import org.company.tasktrack.Fragments.Admin.AddEmployeeFragment;
import org.company.tasktrack.Fragments.Admin.ManageFragment;
import org.company.tasktrack.Fragments.Admin.AdminReportFragment;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends BaseActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.container)
    FrameLayout container;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_add:
                replaceFragment(new AddEmployeeFragment());
                break;
            case R.id.navigation_manage:
                replaceFragment(new ManageFragment());
                break;
            case R.id.navigation_reports:
                replaceFragment(new AdminReportFragment());
                break;
        }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        replaceFragment(new AddEmployeeFragment());
        getSupportFragmentManager()
                .addOnBackStackChangedListener(
                        () -> updateBottomNavigationTitle(getSupportFragmentManager().findFragmentById(R.id.container))
                );
    }

    public void updateBottomNavigationTitle(Fragment f) {
        String className = f.getClass().getName();
        if (className.equals(AddEmployeeFragment.class.getName()))
            navigation.getMenu().getItem(0).setChecked(true);
        else if (className.equals(ManageFragment.class.getName()))
            navigation.getMenu().getItem(1).setChecked(true);
        else if (className.equals(AdminReportFragment.class.getName()))
            navigation.getMenu().getItem(2).setChecked(true);
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
           this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

}
