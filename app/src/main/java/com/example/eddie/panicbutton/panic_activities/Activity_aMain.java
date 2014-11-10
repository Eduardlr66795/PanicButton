package com.example.eddie.panicbutton.panic_activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eddie.panicbutton.GPSTracker;
import com.example.eddie.panicbutton.Item;
import com.example.eddie.panicbutton.ItemDetails;
import com.example.eddie.panicbutton.R;

import java.util.ArrayList;
import java.util.Locale;

public class Activity_aMain extends Activity {

    private Button button_walkSafe;
    private Button button_emergency;
    private Button button_location;
    private Button button_quit;
    private ArrayList<Item> list_contacts;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) {
            setContentView(R.layout.activity_main_landscape);
        } else{
//            Toast.makeText(this,"Screen switched to Portrait mode",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main_portrait);
        }
        MainFragment main = new MainFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, main);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        final ArrayList<ItemDetails> image_details = GetSearchResults();
        list_contacts = populate_list(image_details);
        addListenerOnButton();
    }


    private ArrayList<Item> populate_list(ArrayList<ItemDetails> image_details) {
        ArrayList<Item> list_contacts = new ArrayList<Item>();
        for(ItemDetails item: image_details) {
            String name = item.getName();
            String number = item.getNumber();
            list_contacts.add(new Item(number, name));
        }
        return list_contacts;
    }

    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideSystemUi();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_call:
                Intent dialer = new Intent(Intent.ACTION_DIAL);
                startActivity(dialer);
                return true;

            case R.id.action_speech:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, 1234);
                return true;


            case R.id.menu_item_share:
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"My cool app");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "Here’s some content to share");
                startActivity(Intent.createChooser(i,"Share"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            voice_text = voice_text.toLowerCase();
            if(voice_text.startsWith("w")) {
                button_walksafe();
            } else if(voice_text.startsWith("e")) {
                button_emergency();
            } else if(voice_text.startsWith("l")) {
                button_location();
            } else if(voice_text.startsWith("q")) {
                button_quit();
            }
        }
    }



    private void addListenerOnButton() {
        final Context context = this;
        button_walkSafe = (Button) findViewById(R.id.button_walkSafe);
        button_walkSafe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
//                onClickWhatsApp(view);
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_walkSafe: // Id of the button
                            button_walksafe();
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });


        button_emergency = (Button) findViewById(R.id.button_emergency);
        button_emergency.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_emergency: // Id of the button
                            button_emergency();
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });


        button_location = (Button) findViewById(R.id.button_locate);
        button_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_locate: // Id of the button
                            button_location();
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });


        button_quit = (Button) findViewById(R.id.button_quit);
        button_quit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_quit: // Id of the button
                            button_quit();
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }



    private void button_walksafe() {
        Intent intent_walksafe = new Intent(Activity_aMain.this, Activity_walkSafe.class);
        startActivity(intent_walksafe);
        finish();
    }



    private void button_emergency() {
        Intent intent = new Intent(Activity_aMain.this, Activity_emergency.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list_contacts", list_contacts);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private void button_location() {
        GPSTracker gps = new GPSTracker(Activity_aMain.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", latitude, longitude, "146.232.212 (Home)", -33.9301592, 18.8628034, "Stellenbosch");
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
            button_location();
        }
    }



    private void button_quit() {
        System.exit(0);
    }



    private ArrayList<ItemDetails> GetSearchResults() {
        ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int Number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                long contactId;
                String displayName, number;
                while (cursor.moveToNext()) {
                    number = changeNumber(cursor.getString(Number));
                    displayName = cursor.getString(displayNameIndex);
                    if (verifyNumber(number)) {
                        ItemDetails item_details = new ItemDetails();
                        item_details.setName(displayName);
                        item_details.setNumber(number);
                        item_details.setImageNumber(1);
                        results.add(item_details);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return results;
    }


    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };


    private String changeNumber(String number) {
        number = number.replaceAll("\\s+", "");
        if(number.contains("+27")) {
            number = number.replace("+27", "0");
        }
        return number;
    }


    private boolean verifyNumber(String number) {
        if(number.contains("*")) {
            return false;
        } else if(number.contains("#")) {
            return false;
        } else if(number.contains("-")) {
            return false;
        }  else if(number.contains("+")) {
            return false;
        }
        return true;
    }
}
