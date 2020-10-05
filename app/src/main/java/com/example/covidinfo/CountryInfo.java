package com.example.covidinfo;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {
    @SerializedName("Country")
    private String country;
    @SerializedName("CountryCode")
    private String countryCode;
    @SerializedName("Province")
    private String province;
    @SerializedName("City")
    private String city;
    @SerializedName("CityCode")
    private String cityCode;
    @SerializedName("Lat")
    private double lat;
    @SerializedName("Lon")
    private double lon;
    @SerializedName("Confirmed")
    private long confirmed;
    @SerializedName("Deaths")
    private long deaths;
    @SerializedName("Recovered")
    private long recovered;
    @SerializedName("Active")
    private long active;
    @SerializedName("Date")
    private String Date;

    public CountryInfo(String country, String countryCode, String province, String city, String cityCode, double lat, double lon, long confirmed, long deaths, long recovered, long active, String date) {
        this.country = country;
        this.countryCode = countryCode;
        this.province = province;
        this.city = city;
        this.cityCode = cityCode;
        this.lat = lat;
        this.lon = lon;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
        Date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public long getRecovered() {
        return recovered;
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
