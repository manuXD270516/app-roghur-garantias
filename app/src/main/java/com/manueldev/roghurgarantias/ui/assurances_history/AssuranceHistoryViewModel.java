package com.manueldev.roghurgarantias.ui.assurances_history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssuranceHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AssuranceHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Assurances History fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}