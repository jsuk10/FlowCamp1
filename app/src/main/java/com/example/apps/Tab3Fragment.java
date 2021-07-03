package com.example.apps;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

public class Tab3Fragment extends Fragment {
    private View view;
    private ListView listView;
    String[] items;
    ArrayList<Tab3MusicClass> songname;
    private MediaPlayer mediaPlayer;
    private Context context;
    ArrayAdapter adapter;

    private Button playButton;
    private Button stopButton;
    private Button nextButton;
    private Boolean isPlay;

    public Tab3Fragment() {
        // Required empty public constructor
    }

    public static Tab3Fragment newInstance() {
        Tab3Fragment tab3_fragment = new Tab3Fragment();
        return tab3_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab3, container, false);
        context = container.getContext();
        songname = new ArrayList<>();
        listView = view.findViewById(R.id.tab3_listView);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, songname);
        mediaPlayer  = new MediaPlayer();
        listView.setAdapter(adapter);
        ImageButton button = (ImageButton) view.findViewById(R.id.tab3_PlayButton);
        button.setOnClickListener(v -> playAndStop(view));
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setListCilck();
        permiction();
        mediaPlayer.start();
        return view;
    }

    public void setListCilck(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View views, int position, long id) {
                Tab3MusicClass selected_item = (Tab3MusicClass)songname.get(position);
                Uri uri = Uri.parse(selected_item.getUri());
                try {
                    mediaPlayer.setDataSource(context, uri);
                    mediaPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "io failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (NullPointerException e){
                    Toast.makeText(getActivity(), "is null", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), selected_item.getUri(), Toast.LENGTH_SHORT).show();
                playAndStop(view);
            }
        });
    }

    public void permiction(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            loadImages();
        }
    }
    private void loadImages() {
        songname.clear();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);

        if(cursor!=null){
            while(cursor.moveToNext()){
                // 사진 경로 Uri 가져오기
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3","").replace("wav","");
                songname.add(new Tab3MusicClass(name, uri));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    public void playAndStop(View v) {
        mediaPlayer.start();
    }

    public void pause(View v) {
//        mediaPlayer.seekTo();
    }

    public void next(View v) {
        mediaPlayer.pause();

//        mediaPlayer.setNextMediaPlayer();
    }

    public void before(View v) {
        mediaPlayer.pause();
    }

    private void getMusicData() {


    }

}