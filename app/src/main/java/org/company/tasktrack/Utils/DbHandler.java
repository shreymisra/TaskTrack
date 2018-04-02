package org.company.tasktrack.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.company.tasktrack.Application.Config;
import org.company.tasktrack.Application.MyApplication;

/**
 * Created by Anurag on 12-11-2016.
 */
public class DbHandler {
    static Context context;
    static SharedPreferences prefs;
    static Gson gson;

    public static void initialize(MyApplication cntxt) {
        context = (Context) cntxt;
        prefs = getSharedPreferences();
        gson = new Gson();
    }

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
    }

    public static void put(String Key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key, value);
        editor.commit();
    }

    public static void put(String Key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key, value);
        editor.commit();
    }

    public static void put(String Key, Boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key, value);
        editor.commit();
    }

    public static void put(String key, Object obj) {
        put(key, gson.toJson(obj));
    }

    public static void putList(String key, ArrayList<Object> objArray) {
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        String[] myStringList = objStrings.toArray(new String[objStrings.size()]);
        prefs.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public static void appendToList(String key, ArrayList<Object> objArray, Class c) {
        objArray.addAll(getList(key, c));
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        String[] myStringList = objStrings.toArray(new String[objStrings.size()]);
        prefs.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public static Boolean contains(String key) {
        return prefs.contains(key);
    }

    public static int get(String Key, int Alternate) {
        return prefs.getInt(Key, Alternate);
    }

    public static String get(String Key, String Alternate) {
        return prefs.getString(Key, Alternate);
    }

    public static Boolean get(String Key, Boolean Alternate) {
        return prefs.getBoolean(Key, Alternate);
    }

    public static <T> T get(String key, Class<T> classOfT) {
        String json = get(key, "");
        Object value = gson.fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return (T) value;
    }

    public static ArrayList<Object> getList(String key, Class<?> mClass) {
        ArrayList<String> objStrings = new ArrayList<String>(Arrays.asList(TextUtils.split(prefs.getString(key, ""), "‚‗‚")));
        ArrayList<Object> objects = new ArrayList<Object>();

        for (String jObjString : objStrings) {
            Object value = gson.fromJson(jObjString, mClass);
            objects.add(value);
        }
        return objects;
    }

    public static void remove(String key) {
        if (DbHandler.contains(key)) {
            prefs.edit().remove(key).apply();
        }
    }

    public static void clearDb() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }


    public static HashSet<String> getCookies() {
        return (HashSet<String>) prefs.getStringSet("cookies", new HashSet<String>());
    }

    public static void setSession() {
        put("sessionSet", true);
    }

    public static Boolean isSessionSet() {
        return get("sessionSet", false);
    }

    public static boolean setCookies(HashSet<String> cookies) {
        SharedPreferences.Editor editor = prefs.edit();
        return editor.putStringSet("cookies", cookies).commit();
    }
}