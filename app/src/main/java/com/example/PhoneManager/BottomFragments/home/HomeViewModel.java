/*
ViewModel类，
专门用于存放应用程序页面所需的数据。它将页面所需的数据从页面中剥离出来，
页面只需要处理用户交互，以及负责展示数据的工作。
 */

package com.example.PhoneManager.BottomFragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}