package com.example.covidinfo.datasources;

import com.example.covidinfo.models.Country;
import com.example.covidinfo.models.CountryInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CovidApi {
    @GET("countries")
    Call<List<Country>> getCountries();

    @GET("dayone/country/{country}")
    Call<List<CountryInfo>> countryInfo(@Path("country") String country);
}
