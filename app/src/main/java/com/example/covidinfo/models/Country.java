package com.example.covidinfo.models;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("Country")
    private String country;
    @SerializedName("Slug")
    private String slug;
    @SerializedName("ISO2")
    private String iso2;

    public Country(String country, String slug, String iso2) {
        this.country = country;
        this.slug = slug;
        this.iso2 = iso2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }
}
