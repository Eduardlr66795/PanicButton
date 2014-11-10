package com.example.eddie.panicbutton.panic_activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eddie.panicbutton.R;


/**
 * Created by eddie on 2014/09/29.
 */
public class Activity_location extends Activity  {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_animation);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
//            case R.id.zoomInOut:
//                ImageView image = (ImageView)findViewById(R.id.imageView1);
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
//                image.startAnimation(animation);
//                return true;
//            case R.id.rotate360:
//                ImageView image1 = (ImageView)findViewById(R.id.imageView1);
//                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
//                image1.startAnimation(animation1);
//                return true;
//            case R.id.fadeInOut:
//                ImageView image2 = (ImageView)findViewById(R.id.imageView1);
//                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
//                image2.startAnimation(animation2);
//                return true;
//
//
        }
        return false;
    }

}
