package com.capstone.catstone_eatmorning.ui.subscribes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubscribesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SubscribesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}