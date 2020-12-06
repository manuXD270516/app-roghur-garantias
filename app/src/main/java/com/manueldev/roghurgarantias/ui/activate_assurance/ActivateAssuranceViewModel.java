package com.manueldev.roghurgarantias.ui.activate_assurance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivateAssuranceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActivateAssuranceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Active Assurance fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}