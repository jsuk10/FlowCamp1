package com.example.apps;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab2Fragment extends Fragment {

    View view;
    public Tab2Fragment() {
        // Required empty public constructor
    }


    public static Tab2Fragment newInstance() {
        Tab2Fragment tab2_fragment = new Tab2Fragment();
        return tab2_fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);
        return view;
    }
}