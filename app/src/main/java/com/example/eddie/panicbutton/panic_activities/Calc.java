package com.example.eddie.panicbutton.panic_activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eddie.panicbutton.Item;
import com.example.eddie.panicbutton.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by eddie on 2014/11/06.
 */
public class Calc extends Activity implements View.OnClickListener {
	private Button one, two, three, four, five, six, seven, eight, nine, zero, clear, enter, back;
    private final static String TAG = "BroadcastService";
    private ArrayList<Item> list_contacts;
    private String value;
    private String str_entered;
    private int countStops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            if (getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().
                heightPixels) {
                setContentView(R.layout.unlock_landscape);
            } else {
                setContentView(R.layout.unlock_portrait);
            }

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
            list_contacts = (ArrayList<Item>) getIntent().getSerializableExtra("list_contacts");
            value = "1234";
            str_entered = "";
            Bundle bundle = getIntent().getExtras();
            countStops = bundle.getInt("int_count");

        if (savedInstanceState == null) {
            startService(new Intent(Calc.this, BroadcastService.class));
        }

            one = (Button) findViewById(R.id.one);
            two = (Button) findViewById(R.id.two);
            three = (Button) findViewById(R.id.three);
            four = (Button) findViewById(R.id.four);
            five = (Button) findViewById(R.id.five);
            six = (Button) findViewById(R.id.six);
            seven = (Button) findViewById(R.id.seven);
            eight = (Button) findViewById(R.id.eight);
            nine = (Button) findViewById(R.id.nine);
            zero = (Button) findViewById(R.id.zero);
            clear = (Button) findViewById(R.id.clear);
            enter = (Button) findViewById(R.id.enter);
            back = (Button) findViewById(R.id.back);

            try {
                one.setOnClickListener(this);
                two.setOnClickListener(this);
                three.setOnClickListener(this);
                four.setOnClickListener(this);
                five.setOnClickListener(this);
                six.setOnClickListener(this);
                seven.setOnClickListener(this);
                eight.setOnClickListener(this);
                nine.setOnClickListener(this);
                zero.setOnClickListener(this);
                clear.setOnClickListener(this);
                enter.setOnClickListener(this);
                back.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            if(millisUntilFinished == 0) {
                Intent intent2 = new Intent(getApplicationContext(), Activity_emergency.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list_contacts", list_contacts);
                intent2.putExtras(bundle);
                startActivity(intent2);
                finish();

                try {
                    stopService(new Intent(this, BroadcastService.class));
                    unregisterReceiver(br);
                } catch (Exception e) {
                    // Receiver was probably already stopped in onPause()
                }

            } else {
                final TextView _tv = (TextView) findViewById(R.id.timer);
                _tv.setText(new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
                Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
            }
        }
    }


	@Override
	public void onClick(View view) {
         if(str_entered.length() <= 4) {
            switch(view.getId()){
                case R.id.one:
                    update("1");
                    break;

                case R.id.two:
                    update("2");
                    break;


                case R.id.three:
                    update("3");
                    break;


                case R.id.four:
                    update("4");
                    break;


                case R.id.five:
                    update("5");
                    break;


                case R.id.six:
                    update("6");
                    break;


                case R.id.seven:
                    update("7");
                    break;


                case R.id.eight:
                    update("8");
                    break;


                case R.id.nine:
                    update("9");
                    break;


                case R.id.zero:
                    update("0");
                    break;


                case R.id.clear:
                    action_clear();
                    break;

                case R.id.enter:
                    action_enter();
                    break;

                case R.id.back:
                    action_back();
                    break;
            }
        } else {
            action_clear();
        }
	}


    private void action_clear() {
        for(int a = 0; a < 4; a++) {
            if(a == 0) {
                final TextView view = (TextView) findViewById(R.id.text_password_1);
                view.setText("  ");
            } else if(a == 1) {
                final TextView view = (TextView) findViewById(R.id.text_password_2);
                view.setText("  ");
            } else if(a == 2) {
                final TextView view = (TextView) findViewById(R.id.text_password_3);
                view.setText("  ");
            } else {
                final TextView view = (TextView) findViewById(R.id.text_password_4);
                view.setText("  ");
            }
        }
        str_entered = "";
    }


    private void update(String str_append_value) {
        if(str_entered.length() == 0) {
            final TextView view = (TextView) findViewById(R.id.text_password_1);
            view.setText("*");
        } else if(str_entered.length() == 1) {
            final TextView view = (TextView) findViewById(R.id.text_password_2);
            view.setText("*");
        } else if(str_entered.length() == 2) {
            final TextView view = (TextView) findViewById(R.id.text_password_3);
            view.setText("*");
        } else if(str_entered.length() == 3) {
            final TextView view = (TextView) findViewById(R.id.text_password_4);
            view.setText("*");
        }
        str_entered = str_entered.concat(str_append_value);
    }



    private void action_enter() {
        if(str_entered.equals(value)) {
            Intent intent = new Intent(Calc.this, Activity_aMain.class);
            try {
                stopService(new Intent(this, BroadcastService.class));
                unregisterReceiver(br);
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);
            finish();
        } else {
            action_clear();
        }
    }



    private void action_back() {
        Intent intent = new Intent(Calc.this, Activity_walkSafe_button.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list_contacts", list_contacts);
        intent.putExtras(bundle);
        intent.putExtra("disable_button", true);
        intent.putExtra("start_timer", true);
        intent.putExtra("int_count", countStops);
        try {
            stopService(new Intent(this, BroadcastService.class));
            unregisterReceiver(br);
        } catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(intent);
        finish();
    }
}