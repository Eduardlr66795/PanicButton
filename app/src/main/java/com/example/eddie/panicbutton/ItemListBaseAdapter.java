package com.example.eddie.panicbutton;

/**
 * Created by eddie on 2014/09/30.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListBaseAdapter extends BaseAdapter {
    private static ArrayList<ItemDetails> itemDetailsrrayList;
    private static final String TAG = "MyActivity";

    public static class ViewHolder {
        TextView txt_item_name;
        TextView txt_item_number;
        ImageView item_image;
        CheckBox item_checkbox;
    }

    private Integer[] imgid = {
            R.drawable.contacts,
            R.drawable.contacts,
            R.drawable.contacts,
            R.drawable.contacts,
            R.drawable.contacts,
            R.drawable.contacts
    };

    private LayoutInflater l_Inflater;

    public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results) {
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.item_details_view, null);
            holder = new ViewHolder();
            holder.txt_item_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.txt_item_number = (TextView) convertView.findViewById(R.id.item_number);
            holder.item_image = (ImageView) convertView.findViewById(R.id.item_photo);
            holder.item_checkbox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_item_name.setText(itemDetailsrrayList.get(position).getName());
        holder.txt_item_number.setText(itemDetailsrrayList.get(position).getNumber());
        holder.item_image.setImageResource(imgid[itemDetailsrrayList.get(position).getImageNumber() - 1]);
        holder.item_checkbox.setChecked(itemDetailsrrayList.get(position).getCheckBox());

        return convertView;
    }

    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
//        Toast.makeText(Activity_walkSafe.this, "Activity", Toast.LENGTH_SHORT).show();


        Log.d(TAG, "THIS IS THE POSITION=" + position);
    }






}