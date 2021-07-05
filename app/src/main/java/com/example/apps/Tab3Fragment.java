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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

public class Tab3Fragment extends Fragment {
    private View view;
    private ListView listView;
    private ArrayList<Tab3MusicClass> songlist;
    private MediaPlayer mediaPlayer;
    private Context context;
    private Integer position = 0;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton playAndStopButton;
    private TextView text;

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
        init();
        addButtonEvent();
        setMoveText();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe 시 진행할 동작 */
                loadMusic();
                /* 업데이트가 끝났음을 알림 */
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        setListCilck();
        permiction();
        return view;
    }

    private void setMoveText() {
        text.setSelected(true);
//        listView.setSelected(true);
//        listView.requestFocus();
    }

    public void init(){
        listView = view.findViewById(R.id.tab3_listView);
        songlist = new ArrayList<>();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, songlist);
        mediaPlayer = new MediaPlayer();
        listView.setAdapter(adapter);
        playAndStopButton = view.findViewById(R.id.tab3_PlayButton);
        text = (TextView) view.findViewById(R.id.tab3_musicTitle);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        swipeRefreshLayout = view.findViewById(R.id.tab3_swiperefresh);
    }
    public void addButtonEvent(){
        view.findViewById(R.id.tab3_PlayButton).setOnClickListener(v -> playAndStop(view));
        view.findViewById(R.id.tab3_beforeButton).setOnClickListener(v -> before(view));
        view.findViewById(R.id.tab3_nextButton).setOnClickListener(v -> next(view));
    }

    public void setListCilck() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View views, int position, long id) {
                Tab3Fragment.this.position = position;
                PlaySound(position);
            }
        });
    }

    public void PlaySound(Integer position) {
        Tab3MusicClass selected_item = (Tab3MusicClass) songlist.get(position);
        Uri uri = Uri.parse(selected_item.getUri());
//        if (mediaPlayer.isPlaying())
        mediaPlayer.reset();
        text.setText(selected_item.toString());

        try {
            mediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Uri io failed", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "Uri is null", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "Uri state wrong응", Toast.LENGTH_SHORT).show();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "io failed", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "is null", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "응", Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.start();
        playAndStopButton.setImageResource(android.R.drawable.ic_media_pause);
    }


    public void permiction() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            loadMusic();
        }
    }

    private void loadMusic() {
        songlist.clear();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // 사진 경로 Uri 가져오기
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3", "").replace("wav", "");
                songlist.add(new Tab3MusicClass(name, uri));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    public void playAndStop(View v) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playAndStopButton.setImageResource(android.R.drawable.ic_media_play );
        }
        else {
            mediaPlayer.start();
            playAndStopButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }


    public void next(View v) {
        if(position != songlist.size()-1)
            position +=1;
        else
            position = 0;
        PlaySound(position);
    }

    public void before(View v) {
        if(position != 0)
            position -= 1;
        else
            position = songlist.size()-1;
        PlaySound(position);
    }

}
