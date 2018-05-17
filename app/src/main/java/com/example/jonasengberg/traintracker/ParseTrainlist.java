package com.example.jonasengberg.traintracker;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ParseTrainlist extends AsyncTask <String, Void, GetTrainlist>{

    public GetTrainlist parsedList(String jsonString)
    {
        try {

            JSONArray jsonArray = new JSONArray(jsonString);
            ArrayList<String> newList = new ArrayList<>();
            DateFormatter df = new DateFormatter();

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject allObj = jsonArray.getJSONObject(i);
                JSONArray timeTableArray = allObj.getJSONArray("timeTableRows");
                JSONObject originObj = timeTableArray.getJSONObject(0);
                JSONObject destinationObj = timeTableArray.getJSONObject(timeTableArray.length() - 1);

                int trainNumber = allObj.getInt("trainNumber");
                String trainType = allObj.getString("trainType");

                String origin = originObj.getString("stationShortCode");
                String destination = destinationObj.getString("stationShortCode");

                String departTime = originObj.getString("scheduledTime");
                String arriveTime = destinationObj.getString("scheduledTime");

                newList.add(trainType + "," + trainNumber + "," + "\n" + "Departs from, " + origin + ", " + df.formatDate(departTime) + "," + "\n" + "Arrives at, "+destination + ", " + df.formatDate(arriveTime));
            }

            return new GetTrainlist(newList);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("DEBUG JSON: " + e.getMessage());
        }
        return null;
    }


    @Override
    protected GetTrainlist doInBackground(String... strings) {
        String jsonString = "";

        try
        {
            HttpHandler httpHandler = new HttpHandler();
            jsonString = httpHandler.readUrl(strings[0]);
            return parsedList(jsonString);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("DEBUG IO: " + e.getMessage());
        }

        return null;
    }
}
