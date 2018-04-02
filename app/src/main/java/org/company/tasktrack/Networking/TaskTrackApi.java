package org.company.tasktrack.Networking;

import android.util.Base64;

import org.company.tasktrack.Application.MyApplication;
import org.company.tasktrack.Utils.AddCookiesInterceptor;
import org.company.tasktrack.Utils.ReceivedCookiesInterceptor;
import org.company.tasktrack.Utils.Utils;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shrey on 2/4/18.
 */

public class TaskTrackApi  {
    public static String API_BASE_URL="";

    public static Retrofit getRetrofit(final Boolean cache)
    {
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .cache(new Cache(MyApplication.context.getCacheDir(),10*1024*1024))
                .addInterceptor(chain->{
                    Request request=chain.request();
                    if(cache)
                    if(Utils.networkAvailable())
                    {
                        request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                    }
                    else
                    {
                        request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                    }
                    return chain.proceed(request).newBuilder().removeHeader("Pragma").build();
                })
                .addInterceptor(new ReceivedCookiesInterceptor())
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    public static Retrofit getRetrofit(String username, String password) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        username = username.trim();
        password = password.trim();
        final String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .addInterceptor(logging)
                .addInterceptor(new ReceivedCookiesInterceptor()).build();

        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
