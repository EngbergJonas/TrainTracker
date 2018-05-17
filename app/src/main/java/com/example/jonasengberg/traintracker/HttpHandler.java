package com.example.jonasengberg.traintracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class HttpHandler {

    public String readUrl(String urlString) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;

        try
        {
            URL url = new URL(urlString);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";

            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("DEBUG MALFORM: " + e.getMessage());
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("DEBUG IO HTTP: " + e.getMessage());
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}