package com.sushi.fileUploader.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
   public static final String BASE_URL = "http://13.235.5.17/ukpfmstool/api/";
 //  public static final String BASE_URL = "http://13.126.63.14/ukpfmstool/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60,TimeUnit.SECONDS);
        client.writeTimeout(60,TimeUnit.SECONDS);
        client.connectTimeout(60,TimeUnit.SECONDS);
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        client.addInterceptor(interceptor);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }



}
