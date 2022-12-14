package com.sushi.fileUploader.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sushi.fileUploader.activities.LocationLiveData;

public class LocationViewModel extends AndroidViewModel {
    private Context context;
    private LocationLiveData locationLiveData;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        context=application;
        locationLiveData = new LocationLiveData(context);
    }

    public LocationLiveData getLocationLiveData() {
        return locationLiveData;
    }
}
