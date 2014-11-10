package com.example.eddie.panicbutton.panic_activities;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by eddie on 2014/11/09.
 */
public class MyService extends Service {

    MyCounter timer;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        timer = new MyCounter(11000, 1000);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        timer.start();
        return super.onStartCommand(intent, flags, startId);
    }
    private class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Toast.makeText(getApplicationContext(), "death", Toast.LENGTH_LONG).show();
            stopSelf();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Toast.makeText(getApplicationContext(), (millisUntilFinished / 1000) + " NEW", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        timer.cancel();
        //stopSelf();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}