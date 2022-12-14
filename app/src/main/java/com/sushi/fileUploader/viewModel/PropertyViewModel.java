package com.sushi.fileUploader.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sushi.fileUploader.models.Floor;
import com.sushi.fileUploader.repository.Repository;

import java.util.ArrayList;
import java.util.List;


public class PropertyViewModel extends ViewModel {
    private Repository repository;
    private MutableLiveData<Boolean> isLoading;

    public void init(){
        repository = Repository.getInstance();
    }
    public void insertFloorRecord(Floor floor){
        List<Floor> floors = new ArrayList<>();
        floors.add(floor);
        repository.insertFloorRecord(floors);
        repository.InsertDataToServer(floors);

    }


    public MutableLiveData<Boolean> getIsLoading() {
        return repository.getProgressDialogState();
    }


}

