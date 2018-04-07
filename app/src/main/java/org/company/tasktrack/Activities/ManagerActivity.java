package org.company.tasktrack.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.TextView;

import org.company.tasktrack.Fragments.Admin.AddEmployeeFragment;
import org.company.tasktrack.Fragments.Admin.ManageFragment;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagerActivity extends BaseActivity {

    private TextView mTextMessage;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_assignTask:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_reports2:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

    }

    public void updateBottomNavigationTitle(Fragment f) {
        String className = f.getClass().getName();
        if (className.equals(AddEmployeeFragment.class.getName()))
            navigation.getMenu().getItem(0).setChecked(true);
        else if (className.equals(ManageFragment.class.getName()))
            navigation.getMenu().getItem(1).setChecked(true);
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
