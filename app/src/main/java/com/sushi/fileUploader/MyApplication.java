package com.sushi.fileUploader;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    public static Retrofit mRetrofitDynamic;
    public Retrofit mRetrofit;
    private static MyApplication mInstance;
    private static WeakReference<Context> context;
    public static final String BASE_URL = "http://13.235.5.17/ukpfmstool/api/";
    public static final String CHANNEL_ID = "Service code Running";
    @Override
    public void onCreate() {
        context = new WeakReference<Context>(MyApplication.this);
        mInstance = this;
        super.onCreate();
        startChannelNotification();
    }

    private void startChannelNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "My Service", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build();


}
