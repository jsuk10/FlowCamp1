package com.example.apps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    static ArrayList<MusicFiles> musicFile = new ArrayList<>();
    static ArrayList<Tab3ListViewItem> musicFile = new ArrayList<>();
    private TabLayout tabLayout;
    final private ArrayList<String> tabNames = new ArrayList<>();
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission();
        loadTabName();
        setTabLayout();
        setViewPager();

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setTabLayout() {
        tabLayout = findViewById(R.id.tab);
        tabNames.forEach(name -> tabLayout.addTab(tabLayout.newTab().setText(name)));
    }

    private void loadTabName() {
        tabNames.add("Contacts");
        tabNames.add("Gallery");
        tabNames.add("Music");
    }

    private void setViewPager() {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ff0000"));

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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicFile = getAllAudio(this);
            }
        }else{
            permission();
        }
    }
    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            musicFile = getAllAudio(this);
        }
    }


    public static ArrayList<Tab3ListViewItem> getAllAudio(Context context) {
        ArrayList<Tab3ListViewItem> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] items = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.  Audio.Media.ARTIST,
        };
        Cursor cursor = context.getContentResolver().query(uri, items,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                Tab3ListViewItem item = new Tab3ListViewItem(path, title, artist, album, duration);
                Log.e("Path" + path, "Album : " + album);
                tempAudioList.add(item);
            }
            cursor.close();
        }
        return tempAudioList;
    }


}
