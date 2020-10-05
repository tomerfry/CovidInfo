package com.example.covidinfo.viewmodel;

import com.example.covidinfo.Repository;
import com.example.covidinfo.models.CountryInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<CountryInfo>> mInfosLiveData;
    private MutableLiveData<List<String>> mCountries;
    private Repository repo;
    private MutableLiveData<Boolean> isWaiting = new MutableLiveData<>();

    public void init() {
        this.repo = Repository.getInstance();
        this.mCountries = this.repo.getCountries();
        this.mInfosLiveData = null;
    }

    public LiveData<List<String>> getCountries() {
        return this.mCountries;
    }

    public LiveData<List<CountryInfo>> getCountryInfos(String country) {
        if (this.mInfosLiveData != null) {
            return this.mInfosLiveData;
        }
        this.mInfosLiveData = this.repo.getCountryInfos(country);
        return mInfosLiveData;
    }

}
