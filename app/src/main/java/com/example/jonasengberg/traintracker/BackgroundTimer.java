package com.example.jonasengberg.traintracker;

import android.os.AsyncTask;


public class BackgroundTimer extends AsyncTask<Void, Integer,Void>
{
    MapsActivity ma;

    public BackgroundTimer(MapsActivity ma)
    {
        this.ma = ma;
    }

    /*
    protected void onProgressUpdate(Integer[] number)
    {
        ma.updateNumber(number[0]);
    }
    */

    @Override
    protected Void doInBackground(Void... voids)
    {
        int i = 0;
        for(int number = 1; number < 16; number++)
        {

            if(number == 15)
            {
                i++;
                publishProgress(i);
                number = 0;
            }

            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
