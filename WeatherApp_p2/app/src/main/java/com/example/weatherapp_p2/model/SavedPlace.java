package com.example.weatherapp_p2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.checkerframework.checker.interning.qual.CompareToMethod;

@Entity(tableName = "favorito",
indices = {@Index(value = {"userId"}, unique = true)}
)
public class SavedPlace {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "userId")
    private String userId;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "state")
    private String state;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "lat")
    private double latitude;
    @ColumnInfo(name = "lon")
    private double longitude;

    public SavedPlace() { }

    public SavedPlace(String userId, String city, String state, String country, double latitude, double longitude) {
        this.userId = userId;
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
}