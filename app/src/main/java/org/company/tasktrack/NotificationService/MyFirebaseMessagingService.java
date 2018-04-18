package org.company.tasktrack.NotificationService;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.company.tasktrack.Activities.NotificationsActivity;
import org.company.tasktrack.Networking.Models.NotificationModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.company.tasktrack.Application.Config;
import org.company.tasktrack.Utils.DbHandler;
import org.company.tasktrack.Utils.NotificationUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, String.valueOf(remoteMessage));
        if (remoteMessage == null)
            return;
        if(!DbHandler.getBoolean(getApplicationContext(),"isLoggedIn",false))
            return;
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
               // Log.e("hello", String.valueOf(json));
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
        }
    }


    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject data = json.getJSONObject("data");
            Log.e("dat12", String.valueOf(data));
            NotificationModel notification = new NotificationModel();
            notification.setTitle(data.getString("title"));
            notification.setMessage(data.getString("message"));
            notification.setDate(data.getString("date"));
            notification.setPerson(data.getString("person"));
            notification.setImageUrl(data.getString("imageUrl"));

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", notification.getMessage());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            }
            Intent resultIntent = new Intent(getApplicationContext(), NotificationsActivity.class);
            resultIntent.putExtra("message", notification.getMessage());
            if (TextUtils.isEmpty(notification.getImageUrl())) {
                //Toast.makeText(getApplicationContext(),"if",Toast.LENGTH_LONG).show();
                showNotificationMessage(getApplicationContext(), notification.getTitle(), notification.getMessage(), notification.getTimestamp(), resultIntent);
            } else {
                //Toast.makeText(getApplicationContext(),"else",Toast.LENGTH_LONG).show();

                showNotificationMessageWithBigImage(getApplicationContext(), notification.getTitle(), notification.getMessage(), notification.getMessage(), resultIntent, notification.getImageUrl());
            }
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            DbHandler.putInt(getApplicationContext(),"count",1);
            List<NotificationModel> notificationList = new ArrayList<>();
            String notificationData = DbHandler.getString(getApplicationContext(),"notificationList","");
            if(!notificationData.equals(""))
            {
                notificationList = gson.fromJson(notificationData, new TypeToken<List<NotificationModel>>(){}.getType());
                notificationList.add(notification);
                DbHandler.putString(getApplicationContext(), "notificationList", gson.toJson(notificationList));
            }
            else
            {
                notificationList.add(notification);
                DbHandler.putString(getApplicationContext(), "notificationList", gson.toJson(notificationList));
            }

        }
           catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}