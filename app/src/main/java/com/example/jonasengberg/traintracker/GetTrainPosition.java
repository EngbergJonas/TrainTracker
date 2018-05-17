package com.example.jonasengberg.traintracker;

import com.google.android.gms.maps.model.LatLng;

public class GetTrainPosition {

    LatLng trainPos;
    Integer title;

    public GetTrainPosition(LatLng trainPos, Integer title)
    {
        this.trainPos = trainPos;
        this.title = title;
    }

    public LatLng getTrainPos()
    {
        return trainPos;
    }

    public Integer getTitle()
    {
        return title;
    }
}
