package com.example.eddie.panicbutton.panic_activities;

/**
 * Created by eddie on 2014/10/07.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eddie.panicbutton.GPSTracker;

import java.util.Locale;

public class NotificationView extends Activity {

    private TextView Textv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        GPSTracker gps = new GPSTracker(NotificationView.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", latitude, longitude, "My Location", latitude, longitude, "Panic location");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
            try {
                startActivity(intent);
            } catch(ActivityNotFoundException ex) {
                try {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                }
                catch(ActivityNotFoundException innerEx) {
                    Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            gps.showSettingsAlert();
        }
    }
}