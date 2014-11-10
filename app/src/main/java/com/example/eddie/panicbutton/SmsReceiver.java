package com.example.eddie.panicbutton;

/**
 * Created by eddie on 2014/09/30.
 */

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.eddie.panicbutton.panic_activities.NotificationView;

import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }


            String coordinates[] = str.split("~")[1].split(",");


            Toast.makeText(context, coordinates[0], Toast.LENGTH_SHORT).show();

//            testGoogleMaps(context, str);

            displayNotification(context, intent, coordinates);
//            abortBroadcast();
        }
    }


    protected void testGoogleMaps(Context context, String str) {
        double latitude = Double.parseDouble(str.split("~")[1].split(",")[0]);
        double longitude = Double.parseDouble(str.split("~")[1].split(",")[1]);
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(intent);

    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void displayNotification(Context context, Intent intent, String coordinates[]) {
        Log.i("Start", "notification");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setContent( remoteViews);
        mBuilder = setSound(mBuilder);

        Intent resultIntent = new Intent(context, NotificationView.class);
        Bundle extras = new Bundle();
        extras.putString("latitude",coordinates[0]);
        extras.putString("longitude",coordinates[1]);


        Toast.makeText(context, coordinates[0], Toast.LENGTH_SHORT).show();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//
//        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationView.class);
//
//        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button_openMaps, resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(9999, mBuilder.build());
    }



    protected NotificationCompat.Builder setSound(NotificationCompat.Builder mBuilder) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(soundUri);
        return mBuilder;
    }
}
