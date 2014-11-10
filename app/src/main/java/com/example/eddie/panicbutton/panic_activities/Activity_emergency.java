package com.example.eddie.panicbutton.panic_activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eddie.panicbutton.GPSTracker;
import com.example.eddie.panicbutton.Item;
import com.example.eddie.panicbutton.R;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by eddie on 2014/09/29.
 */
public class Activity_emergency extends Activity {
    Button sendMessage;
    TextView textview_1;

    private int notificationID = 100;
    private int numMessages = 0;
    private NotificationManager mNotificationManager;
    private ArrayList<Item> list_contacts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_emergency);
        Bundle bundle = getIntent().getExtras();
        list_contacts = (ArrayList<Item>) getIntent().getSerializableExtra("list_contacts");
        addListenerOnButton();
//        for(Item item: list_contacts) {
            Item item = new Item("0727296499","Frank");
            sendSMSMessage(item, getApplicationContext());
//        }
    }


    protected String getEmailAddress() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        String possibleEmail = "";
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                 possibleEmail = account.name;
            }
        }
        return possibleEmail.length() != 0 ? null : possibleEmail;

    }

    public void addListenerOnButton() {
        final Context context = this;
        textview_1 = (TextView) findViewById(R.id.activity_emergency);
    }




    protected void showToast(CharSequence text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }


    protected String getNumber() {
        return "0727296499";
    }


    protected String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("MOBILE PANIC BUTTON ENABLED\n~");
        return sb.toString();
    }



    protected void sendSMSMessage(Item item, Context context) {
        GPSTracker gps = new GPSTracker(Activity_emergency.this);
        String phoneNo = item.getNumber();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append(getMessage());

//        check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            smsBody.append("\nLatitude: " + gps.getLatitude() + ",");
            smsBody.append("\nLongitude: " + gps.getLongitude());
            smsBody.append("\n~\n");
            smsBody.append("");
            smsBody.append("\nGoogle maps link:\n");
            String link = "http://maps.google.com?q="+latitude+","+longitude;
            smsBody.append(link);

        } else {
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        sendSMS(phoneNo, smsBody.toString());
    }



    private void sendSMS(String phoneNumber, String message) {
        Toast.makeText(Activity_emergency.this, phoneNumber + " : SMS SENT", Toast.LENGTH_SHORT).show();

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";


        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);


        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
