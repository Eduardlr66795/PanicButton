package com.example.eddie.panicbutton.panic_activities;

/**
 * Created by eddie on 2014/11/09.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eddie.panicbutton.R;

public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }
}