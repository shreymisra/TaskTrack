package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import org.company.tasktrack.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.inputUserName)
    EditText username;
    @BindView(R.id.inputPassword)
    EditText password;
    @BindView(R.id.buttonLogin)
    EditText buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.buttonLogin)
    public void login()
    {

    }
}
