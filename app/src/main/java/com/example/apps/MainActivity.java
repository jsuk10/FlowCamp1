package com.example.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ArrayList <String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTabName();
        setTabLayout();
        setViewPager();

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setTabLayout(){
        tabLayout = findViewById(R.id.tab);
        tabNames.stream().forEach(name ->tabLayout.addTab(tabLayout.newTab().setText(name)));
    }

    private void loadTabName(){
        tabNames.add("탭1");
        tabNames.add("탭2");
        tabNames.add("탭3");
    }

    private void setViewPager() {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}