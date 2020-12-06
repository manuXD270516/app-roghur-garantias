package com.manueldev.roghurgarantias.ui.claim_assurance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClaimAssuranceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClaimAssuranceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}