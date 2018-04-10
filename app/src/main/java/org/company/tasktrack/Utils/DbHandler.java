package org.company.tasktrack.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import org.company.tasktrack.Activities.LoginActivity;
import org.company.tasktrack.Application.Config;

/**
 * Created by Anurag on 12-11-2016.
 */
public class DbHandler {

    public static void putInt(Context context, String Key, int value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(Key, value);
            editor.commit();
        }
    }

    public static void putString(Context context, String Key, String value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, value);
            editor.commit();
        }
    }

    public static void putBoolean(Context context, String Key, Boolean value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Key, value);
            editor.commit();
        }
    }

    public static Boolean contains(Context context, String key){
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.contains(key);
        } else return null;
    }

    public static int getInt(Context context, String Key, int Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);

            return prefs.getInt(Key, Alternate);
        } else return 0;
    }

    public static String getString(Context context, String Key, String Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.getString(Key, Alternate);
        } else return null;
    }

    public static Boolean getBoolean(Context context, String Key, Boolean Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.getBoolean(Key, Alternate);
        } else return false;
    }
    public static void remove(Context context, String key){
        if(context!=null){
            SharedPreferences prefs;
            prefs=context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            if(DbHandler.contains(context,key)) {
                prefs.edit().remove(key).commit();
            }
        }
    }

    public static void clearDb(Context context) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(Config.DB_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    public static void setSession(Context context, String userInfo) {
        if (context != null) {
           // FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
            DbHandler.putString(context, "UserInfo", userInfo);
            DbHandler.putBoolean(context, "isLoggedIn", true);
        }
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void unsetSession(Context context, String type) {
        if (context != null) {
           // FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
            DbHandler.clearDb(context);
            DbHandler.putBoolean(context, "isLoggedIn", false);
            Bundle b = new Bundle();
            b.putBoolean(type, true);
            Intent i = new Intent(context, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtras(b);
            context.startActivity(i);
            ((Activity) context).finishAffinity();
        }
    }
    /*public static void update_unsetSession(Context context, String type) {
        if (context != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
            DbHandler.clearDb(context);
            DbHandler.putBoolean(context, "update", true);
            Bundle b = new Bundle();
            b.putBoolean(type, true);
            Intent i = new Intent(context, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtras(b);
            context.startActivity(i);
            ((Activity) context).finishAffinity();
        }
    }*/
}