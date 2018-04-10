package org.company.tasktrack.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.company.tasktrack.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shrey on 10/4/18.
 */

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.options_rate)
    void onRate() {
        final String my_package_name = getPackageName();
        String url = "";
        try {
            getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "market://details?id=" + my_package_name;
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=" + my_package_name;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    @OnClick(R.id.options_feedback)
    void onFeedback() {
        String mailto = "mailto:shrey.1513097@kiet.edu?subject=" + Uri.encode("APP Feedback");
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install an E-Mail App", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.options_logout)
    void onLogout() {
        logout();
    }


}
