package com.sushi.fileUploader.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.gson.JsonObject;
import com.sushi.fileUploader.models.LoginApiResponse;
import com.sushi.fileUploader.repository.Repository;

public class LoginViewModel extends ViewModel {
    private Repository repository;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<LoginApiResponse> mutableLiveData;

    public void init(JsonObject jsonObject) {
        repository = Repository.getInstance();
        isLoading = repository.getProgressDialogState();
        //mutableLiveData = repository.userSignIn(jsonObject);
    }

    public void init(){
        repository = Repository.getInstance();
    }
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public MutableLiveData<LoginApiResponse>getLogin(JsonObject jsonObject){
      return repository.doUserLogin(jsonObject);
    }



}
