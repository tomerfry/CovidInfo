package com.example.covidinfo;

import com.example.covidinfo.datasources.CovidApi;
import com.example.covidinfo.datasources.CovidInfoProvider;
import com.example.covidinfo.models.Country;
import com.example.covidinfo.models.CountryInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static Repository repository;
    private CovidApi covidApi;

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public Repository() {
        covidApi = CovidInfoProvider.createProvider(CovidApi.class);
    }

    public MutableLiveData<List<String>> getCountries() {
        MutableLiveData<List<String>> countriesLiveData = new MutableLiveData<>();
        this.covidApi.getCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> countries = new ArrayList<>();
                    for (Country country : response.body()) {
                        countries.add(country.getSlug());
                    }
                    Collections.sort(countries);
                    countriesLiveData.setValue(countries);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                countriesLiveData.setValue(null);
            }
        });
        return countriesLiveData;
    }

    public MutableLiveData<List<CountryInfo>> getCountryInfos(String country) {
        MutableLiveData<List<CountryInfo>> countryInfos = new MutableLiveData<>();
        this.covidApi.countryInfo(country).enqueue(new Callback<List<CountryInfo>>() {
            @Override
            public void onResponse(Call<List<CountryInfo>> call, Response<List<CountryInfo>> response) {
                if (response.isSuccessful()) {
                    countryInfos.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CountryInfo>> call, Throwable t) {
                countryInfos.setValue(null);
            }
        });
        return countryInfos;
    }

}
