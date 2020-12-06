package com.manueldev.roghurgarantias.ui.users_commerce;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsersCommerceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsersCommerceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Users Commerce fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}