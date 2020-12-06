package com.manueldev.roghurgarantias.ui.clients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClientViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClientViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Client fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}