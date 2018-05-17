package com.example.jonasengberg.traintracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GetTrainPosition getTrainPosition;
    private Handler handler = new Handler();
    TextView textView;

    String url = "";
    String trainNumber = "";
    String departTime = "";
    String arriveTime = "";
    String[] allContentArray = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get all the necessary info from MainActivity
        Intent intent = getIntent();
        String allContent = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        allContentArray = allContent.split(",");
        trainNumber = allContentArray[1];
        departTime = allContentArray[4];
        arriveTime = allContentArray[allContentArray.length - 1];
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
    }

    //Get trains position on the map
    public void startTracking(View view)
    {
        setTrainPosition();
        handler.removeCallbacks(updateTrainPosition);
        handler.post(updateTrainPosition);
    }

    //Get notifications within 10minute intervals
    public void startNotify(View view)
    {
        setNotification();
        handler.removeCallbacks(updateTrainTimetable);
        handler.post(updateTrainTimetable);
    }


    //Update the position every 20seconds
    private Runnable updateTrainPosition = new Runnable()
    {
        public void run()
        {
            setTrainPosition();
            handler.postDelayed(this, 4000*4);
        }
    };

    //Set the position of the train and begin the tracking
    public void setTrainPosition()
    {
        url = "https://rata.digitraffic.fi/api/v1/train-locations/latest/" + trainNumber;

        try {
            mMap.clear();
            getTrainPosition = new ParseTrainPosition().execute(url).get();
            if (getTrainPosition == null) {
                Toast.makeText(this, trainNumber + ", will depart at " + departTime + ".", Toast.LENGTH_SHORT).show();
                return;
            }

            LatLng trainPos = getTrainPosition.getTrainPos();
            Integer title = getTrainPosition.getTitle();

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(trainPos)
                    .title(title.toString())
                    .snippet("Arrival: " + arriveTime);
            mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trainPos, 12));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Notify the user within 10minutes intervals, when a train will depart
    private Runnable updateTrainTimetable = new Runnable()
    {
        public void run()
        {
            setNotification();
            handler.postDelayed(this, 60000 * 5);
        }
    };

    //Build the notification that appears for the user
    public void setNotification()
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel description");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_train)
                .setContentTitle(trainNumber)
                .setContentText("Will depart at at:" + departTime)
                .setSubText("Have a nice trip!");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
