package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.inputUserName)
    EditText username;
    @BindView(R.id.inputPassword)
    EditText password;
    @BindView(R.id.buttonLogin)
    EditText buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.buttonLogin)
    public void login()
    {
        intentWithFinish(EmployeeActivity.class);
    }
}
