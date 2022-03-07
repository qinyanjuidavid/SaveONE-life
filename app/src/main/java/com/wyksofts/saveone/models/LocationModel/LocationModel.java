package com.wyksofts.saveone.models.LocationModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LocationModel {

    public LocationModel(ArrayList<LatLng> locationArrayList) {
        this.locationArrayList = locationArrayList;
    }

    ArrayList<LatLng> locationArrayList;

    public ArrayList<LatLng> getLocationArrayList() {
        return locationArrayList;
    }

    public void setLocationArrayList(ArrayList<LatLng> locationArrayList) {
        this.locationArrayList = locationArrayList;
    }
}
