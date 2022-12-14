package com.sushi.fileUploader.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.sushi.fileUploader.models.Floor;
import com.sushi.fileUploader.models.LoginApiResponse;
import com.sushi.fileUploader.network.ApiClient;
import com.sushi.fileUploader.request.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    // static method to create instance of Singleton class
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static Repository repository_instance = null;
    private ApiInterface apiInterface;


    private Repository() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }
    // static method to create instance of Singleton class
    public static Repository getInstance() {
        if (repository_instance == null)
            repository_instance = new Repository();
        return repository_instance;
    }
    public MutableLiveData<Boolean> getProgressDialogState() {
        return isLoading;
    }
    /*method for inserting floor record*/
    public void insertFloorRecord(List<Floor> floors) {
        isLoading.setValue(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading.postValue(false);
//                floorDao.insert(floors);
            }
        }).start();
    }

    public void InsertDataToServer(List<Floor> floors){
        isLoading.setValue(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading.postValue(false);
                // call api
                Log.e("ApiCalling","Api is calling ");

            }
        }).start();
    }
    // method for login
    public MutableLiveData<LoginApiResponse> doUserLogin(JsonObject jsonObject) {
        isLoading.setValue(true);
        final MutableLiveData<LoginApiResponse> mutableLiveData = new MutableLiveData<>();
        Call<LoginApiResponse> call = apiInterface.userAuthenticate(jsonObject);
        call.enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(Call<LoginApiResponse> call, Response<LoginApiResponse> response) {
                isLoading.postValue(false);
                if (response != null && response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginApiResponse> call, Throwable t) {
                isLoading.postValue(false);
                t.printStackTrace();
            }
        });
        return mutableLiveData;
    }
}
