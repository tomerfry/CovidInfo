package com.example.covidinfo.datasources;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CovidInfoProvider {

    private static Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.covid19api.com")
            .build();

    public static <S> S createProvider(Class<S> providerClass) {
        return retrofit.create(providerClass);
    }
}
