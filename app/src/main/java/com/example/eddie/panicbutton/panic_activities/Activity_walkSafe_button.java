package com.example.eddie.panicbutton.panic_activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eddie.panicbutton.Item;
import com.example.eddie.panicbutton.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by eddie on 2014/09/30.
 */
public class Activity_walkSafe_button extends Activity {

    private Button button_enable;
    private Button button_disable;
    private ArrayList<Item> list_contacts;
    private boolean bool_button_clicked;
    private boolean bool_time_running;
    private int countStops;
    private CountDownTimer mytimer;
    private int int_maxValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_button);
        hideSystemUi();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if (visibility == 0) {
                            mHideHandler.postDelayed(mHideRunnable, 1);
                        }
                    }
                }

        );

        int_maxValue = 9;
        Bundle bundle = getIntent().getExtras();
        list_contacts = (ArrayList<Item>) getIntent().getSerializableExtra("list_contacts");
        button_enable = (Button) findViewById(R.id.button_panic_enable);
        button_disable = (Button) findViewById(R.id.button_panic_disable);
        button_disable.setEnabled(bundle.getBoolean("disable_button"));


        if(bundle.getBoolean("start_timer")) {
            bool_button_clicked = true;
            bool_time_running = true;
            test_time();
        }
        countStops = bundle.getInt("int_count");

        Toast.makeText(getApplicationContext(), "CS: " + Integer.toString(countStops) , Toast.LENGTH_SHORT).show();

        bool_button_clicked = false;
        button_enable.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_panic_enable: // Id of the button
                            button_disable.setEnabled(true);
                            if (mHandler != null) {
                                return true;
                            } else {
                                if (bool_time_running) {
                                    bool_time_running = false;
                                    bool_button_clicked = false;
                                    mytimer.cancel();
                                    final TextView _tv = (TextView) findViewById(R.id.button_panic_enable);
                                    _tv.setText("Enabled");
                                }

                                mHandler = new Handler();
                                button_enable.setBackgroundColor(Color.BLUE);
                                mHandler.postDelayed(mAction, 500);
                                break;
                            }
                    }
                }
                if (motion.getAction() == MotionEvent.ACTION_UP) {
                    if (mHandler == null) {
                        return true;
                    } else {
                        mHandler.removeCallbacks(mAction);
                        button_enable.setBackgroundColor(Color.LTGRAY);
                        mHandler = null;
                        bool_button_clicked = true;
                        countStops++;
                        bool_time_running = true;
                        //TODO: Change the count stops
                        if(countStops == int_maxValue) {
                            button_enable.setBackgroundColor(Color.LTGRAY);
                            button_disable.setEnabled(false);
                            button_enable.setEnabled(false);

                            Intent intent = new Intent(Activity_walkSafe_button.this, Activity_emergency.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("list_contacts", list_contacts);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            test_time();
                        }
                    }
                }
                return true;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Performing action...");
                    mHandler.postDelayed(this, 1);
                }
            };
        });

        button_disable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_panic_disable: // Id of the button
                            mytimer.cancel();
                            if(countStops >= int_maxValue) {
                                Intent intent = new Intent(Activity_walkSafe_button.this, Activity_emergency.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("list_contacts", list_contacts);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(Activity_walkSafe_button.this, Calc.class);
                                countStops++;
                                intent.putExtra("int_count", countStops);
                                startActivity(intent);
                                finish();
                            }
                    }
                }
                return true;
            }
        });
    }


    private void test_time() {
              final TextView _tv = (TextView) findViewById(R.id.button_panic_enable);
                mytimer = new CountDownTimer(11000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        _tv.setText(new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));

                        Button image1 = (Button)findViewById(R.id.button_panic_enable);
                        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                        image1.startAnimation(animation1);

                    }
                    public void onFinish() {
                        Intent intent = new Intent(Activity_walkSafe_button.this, Activity_emergency.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list_contacts", list_contacts);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(Activity_walkSafe_button.this, "Back", Toast.LENGTH_SHORT).show();
             //Do Code Here
            // If want to block just return false
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Toast.makeText(Activity_walkSafe_button.this, "Menu", Toast.LENGTH_SHORT).show();
            //Do Code Here
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Toast.makeText(Activity_walkSafe_button.this, "HOME", Toast.LENGTH_SHORT).show();

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();


            //Do Code Here
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            Toast.makeText(Activity_walkSafe_button.this, "SEARCH", Toast.LENGTH_SHORT).show();
            //Do Code Here
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            Toast.makeText(Activity_walkSafe_button.this, "SETTINGS", Toast.LENGTH_SHORT).show();
            //Do Code Here
            // If want to block just return false
            return false;
        }
        return false;
    }


    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideSystemUi();
        }
    };
}



