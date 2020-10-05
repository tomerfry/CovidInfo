package com.example.covidinfo;

import com.example.covidinfo.Models.Country;
import com.example.covidinfo.Models.CountryInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CovidApi {
    @GET("countries")
    Observable<List<Country>> getCountries();

    @GET("dayone/country/{country}")
    Observable<List<CountryInfo>> countryInfo(@Path("country") String country);
}
