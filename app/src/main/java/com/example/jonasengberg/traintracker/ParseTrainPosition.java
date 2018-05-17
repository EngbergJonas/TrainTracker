package com.example.jonasengberg.traintracker;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLOutput;

public class ParseTrainPosition extends AsyncTask<String, Integer, GetTrainPosition>{

    private GetTrainPosition parsedString(String jsonString)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray array = jsonObject.getJSONObject("location").getJSONArray("coordinates");

            LatLng trainPos = new LatLng(array.getDouble(1), array.getDouble(0));
            Integer title = jsonObject.getInt("trainNumber");

            return new GetTrainPosition(trainPos, title);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected GetTrainPosition doInBackground(String... strings) {


                String jsonString = "";
                try
                {
                    HttpHandler httpHandler = new HttpHandler();
                    jsonString = httpHandler.readUrl(strings[0]);
                    return parsedString(jsonString);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("DEBUG IO: " + e.getMessage());
                }
                publishProgress();
        return null;
    }
}
