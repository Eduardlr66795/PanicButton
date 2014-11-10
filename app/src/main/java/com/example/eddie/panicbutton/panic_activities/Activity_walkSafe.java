package com.example.eddie.panicbutton.panic_activities;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eddie.panicbutton.Item;
import com.example.eddie.panicbutton.ItemDetails;
import com.example.eddie.panicbutton.ItemListBaseAdapter;
import com.example.eddie.panicbutton.R;

import java.util.ArrayList;



public class Activity_walkSafe extends Activity {

    private ItemListBaseAdapter item_adapter;
    private CheckBox checkBox_selectAll;
    public  final Context context = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_walksafe);
        hideSystemUi();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if (visibility == 0) {
                            mHideHandler.postDelayed(mHideRunnable, 1);
                        }
                    }
                });


        /*
            This is when the list item is selected
         */
        final ArrayList<ItemDetails> image_details = GetSearchResults();
        final ListView item_listView = (ListView) findViewById(R.id.listV_main);
        item_adapter = new ItemListBaseAdapter(this, image_details);
        item_listView.setAdapter(item_adapter);
        item_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = item_listView.getItemAtPosition(position);
                ItemDetails obj_itemDetails = (ItemDetails)o;
                if(obj_itemDetails.getCheckBox() == false) {
                    image_details.get(position).setCheckBox(true);
                } else {
                    image_details.get(position).setCheckBox(false);
                }

                CheckBox cb = (CheckBox)v.findViewById(R.id.item_checkbox);
                cb.setChecked(true);
                item_adapter.notifyDataSetChanged();
            }
        });



         /*
            This is when the SelectAll checkbox is selected
         */
        checkBox_selectAll = (CheckBox) findViewById(R.id.check_box_selectAll);
        checkBox_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(checkBox_selectAll.isChecked()) {
                    for(ItemDetails item: image_details) {
                        item.setCheckBox(true);
                    }
                    item_adapter.notifyDataSetChanged();
                } else {
                    for(ItemDetails item: image_details) {
                        item.setCheckBox(false);
                    }
                    item_adapter.notifyDataSetChanged();
                }
            }
        });


        Button button_next = (Button) findViewById(R.id.button_walkSafe_next);
        button_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_walkSafe_next: // Id of the button
                            ArrayList<Item> list_contacts = new ArrayList<Item>();
                            for (ItemDetails item : image_details) {
                                if(item.getCheckBox()) {
                                    String name = item.getName();
                                    String number = item.getNumber();
                                    list_contacts.add(new Item(number, name));
                                }
                            }
                            if(list_contacts.size() >= 1) {
                                Intent intent = new Intent(Activity_walkSafe.this, Activity_walkSafe_button.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("list_contacts", list_contacts);
                                intent.putExtras(bundle);
                                intent.putExtra("disable_button", false);
                                intent.putExtra("start_timer", false);
                                intent.putExtra("int_count", 0);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Activity_walkSafe.this, "Not enough contacts selected", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });


        Button button_back = (Button) findViewById(R.id.button_walksafe_back);
        button_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion) {
                if (motion.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.button_walksafe_back: // Id of the button
                            Intent intent = new Intent(Activity_walkSafe.this, Activity_aMain.class);
                            startActivity(intent);
                            finish();
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }



    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            ActionBar ab = getActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.app_name);
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            ab.show();
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

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


    private boolean isCellphone(String number) {
        return false;
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

}