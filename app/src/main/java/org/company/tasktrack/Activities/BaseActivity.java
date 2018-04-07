package org.company.tasktrack.Activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.company.tasktrack.Application.MyApplication;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;
import okhttp3.Cache;
import retrofit2.HttpException;


/**
 * Created by anurag on 5/10/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected static MaterialDialog progressBar, alert;
    static Gson gson;
    public List<Disposable> disposables;
    public Observable<Connectivity> networkObservable;
    protected Bundle intentExtras;
    ProgressBar defaultProgressBar;
    Snackbar snackbar;
    Dialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentExtras = getIntent().getExtras() != null ? getIntent().getExtras() : new Bundle();
        disposables = new ArrayList<>();
        snackbar = Snackbar.make(findViewById(android.R.id.content), "No network connectivity", Snackbar.LENGTH_INDEFINITE);
        progressBar = new MaterialDialog.Builder(this)
                .title("Processing")
                .content("Please Wait..")
                .cancelable(false)
                .progress(true, 0).build();
        alert = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT).build();
        gson = new Gson();
        pd = new Dialog(this, android.R.style.Theme_Black);
        View view = getLayoutInflater().inflate(R.layout.remove_border, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        pd.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onNetworkChange(Connectivity connectivity) {

    }

    public void intentWithFinish(Class cls) {
        intentWithoutFinish(cls);
        finish();
    }

    public void intentWithoutFinish(Class cls) {
        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtras(intentExtras);
        startActivity(i);
    }

    public Bundle getIntentExtras() {
        return intentExtras;
    }

    public void putObjectInIntentExtras(String key, Object obj) {
        getIntentExtras().putString(key, gson.toJson(obj));
    }

    public <T> T getObjectFromIntentExtras(String key, Class<T> classOfT) {
        String json = getIntentExtras().getString(key);
        Object value = gson.fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return (T) value;
    }

    public synchronized void handleNetworkErrors(Throwable e, int type) {
        hideDefaultProgress();
        getProgressBar().dismiss();
        e.printStackTrace();
        if (e instanceof HttpException) {
            int status = ((HttpException) e).response().code();

            Log.e("Status", String.valueOf(status));
            if (status == 400)
                alert("Error", "Invalid Username and Password.", type);
            else if (status == 401) {
                if (getClass().equals(LoginActivity.class))
                    alert("Error", "You Are Unauthorized.", type);
                else
                    invalidateSession();
            }
            else if (status == 403)
                alert("Error", "You Don't Have Permission to access this.", type);
            else if (status == 405)
                alert("Error", "Kindly Contact the Developers.", type);
            else if (status == 408)
                alert("Error", "Time Limit Exceeded.", type);
            else if (status == 404)
                alert("Error", "Resource Not Found.", type);
            else if (status == 415)
                alert("Error", "This File Is Not Supported.", type);
            else if (status == 429)
                alert("Error", "You Made Too Many Attempts Kindly Wait For Some Time.", type);
            else if (status == 500)
                alert("Error", "Your Request Could Not Be Processed At The Moment.", type);
            else if (status == 502)
                alert("Error", "No Response from server.", type);
            else if (status == 503)
                alert("Error", "This Service Is Currently Unavailable.", type);
            else if (status == 511)
                alert("Error", "Kindly login to Captive Portal.", type);
            else if (status == 409)
                alert("Conflict", "Entry Already Found.", type);
            else if (status == 204)
                alert("Conflict", "Data Requested Could Not Be Found.", type);
            else
                alert("Error", "Error occured. Please try again later.", type);
        } else if (e instanceof SocketTimeoutException) {
            alert("Error", "Connection timeout.", type);
        } else if (e instanceof IOException) {
            if (e instanceof ConnectException) ;
            alert("Error", "Please check network connectivity.", type);
        } else {
            alert("Error", "Unknown error occured.", type);
        }
    }

    public void alert(String title, String content, int type) {
        if (type != -1) {
            alert.getBuilder().title(title).content(content).negativeText("OK").show();
        }
    }

    public MaterialDialog getProgressBar() {
        return progressBar;
    }

    public void invalidateSession() {
        getIntentExtras().putBoolean("sessionOut", true);
        DbHandler.clearDb();
        intentWithFinish(LoginActivity.class);
    }

    public void logout() {
        getIntentExtras().putBoolean("loggedOut", true);
        try {
            new Cache(MyApplication.context.getCacheDir(), 10 * 1024 * 1024).evictAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DbHandler.clearDb();
        intentWithFinish(LoginActivity.class);
    }

    public void setDefaultProgressBar(ProgressBar progressBar) {
        defaultProgressBar = progressBar;
    }

    public void showDefaultProgress() {
        if (defaultProgressBar != null && defaultProgressBar.getVisibility() == View.INVISIBLE)
            defaultProgressBar.setVisibility(View.VISIBLE);
//        pd.show();
    }

    public void hideDefaultProgress() {
        if (defaultProgressBar != null && defaultProgressBar.getVisibility() == View.VISIBLE)
            defaultProgressBar.setVisibility(View.INVISIBLE);
        pd.hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

}
