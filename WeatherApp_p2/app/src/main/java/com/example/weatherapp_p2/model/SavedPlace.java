package com.example.weatherapp_p2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.checkerframework.checker.interning.qual.CompareToMethod;

@Entity(tableName = "favorito",
indices = {@Index(value = {"userId"}, unique = false)}
)
public class SavedPlace {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "userId")
    private String userId;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "cityId")
    private long cityId;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "lat")
    private double latitude;
    @ColumnInfo(name = "lon")
    private double longitude;
    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public SavedPlace() { }

    public SavedPlace(String userId, String city, long cityId, String country, double latitude, double longitude, boolean isActive) {
        this.userId = userId;
        this.city = city;
        this.cityId = cityId;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { this.isActive = active; }

}