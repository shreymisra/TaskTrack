package org.company.tasktrack.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.company.tasktrack.Adapters.NotificationAdapter;
import org.company.tasktrack.Networking.Models.NotificationModel;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity {

    Gson gson;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.bell)
    ImageView bell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gson=new Gson();
        List<NotificationModel> notificationList = new ArrayList<>();
        String notificationData = DbHandler.getString(getApplicationContext(), "notificationList", "");
        if (!notificationData.equals("")) {
            notificationList = gson.fromJson(notificationData, new TypeToken<List<NotificationModel>>() {
            }.getType());
            Collections.reverse(notificationList);
        }
        if (notificationList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
           bell.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            bell.setVisibility(View.GONE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NotificationAdapter(this,notificationList));
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
