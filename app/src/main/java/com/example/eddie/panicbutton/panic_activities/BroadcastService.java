package com.example.eddie.panicbutton.panic_activities;

/**
 * Created by eddie on 2014/11/07.
 */

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class BroadcastService extends Service {

    private final static String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);
    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bi.putExtra("countdown", millisUntilFinished);
//                Toast.makeText(getApplicationContext(), (millisUntilFinished / 1000) + "", Toast.LENGTH_SHORT).show();
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                bi.putExtra("countdown", 0);
                sendBroadcast(bi);
                Log.i(TAG, "Timer finished");
                cdt.cancel();
            }
        };
        cdt.start();
    }

    @Override
    public void onDestroy() {
        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}