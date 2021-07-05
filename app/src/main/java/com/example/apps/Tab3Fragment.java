package com.example.apps;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ArrayList<Tab3ListViewItem> songlist;
    private MediaPlayer mediaPlayer;
    private Context context;
    private Integer position = 0;
    private Tab3ListViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton btnPlayAndStop;


    private Uri uriCurrMusic;
    private ImageView imgCurrAlbumArt;
    private TextView txtCurrTitle;
    private TextView txtCurrArtist;

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
        txtCurrTitle.setSelected(true);
    }

    public void init(){
        listView = view.findViewById(R.id.tab3_listView);
        songlist = new ArrayList<>();
        adapter = new Tab3ListViewAdapter();
        mediaPlayer = new MediaPlayer();
        listView.setAdapter(adapter);
        btnPlayAndStop = view.findViewById(R.id.tab3_playButton);

        imgCurrAlbumArt = (ImageView) view.findViewById(R.id.tab3_imgAlbumArt);
        txtCurrTitle = (TextView) view.findViewById(R.id.tab3_txtTitle);
        txtCurrArtist = (TextView) view.findViewById(R.id.tab3_txtArtist);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        swipeRefreshLayout = view.findViewById(R.id.tab3_swipeRefresh);
    }
    public void addButtonEvent(){
        view.findViewById(R.id.tab3_playButton).setOnClickListener(v -> playAndStop(view));
        view.findViewById(R.id.tab3_prevButton).setOnClickListener(v -> prev(view));
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
        Tab3ListViewItem selected_item = (Tab3ListViewItem) songlist.get(position);

        mediaPlayer.reset();
        uriCurrMusic = Uri.parse(selected_item.getUri());

        imgCurrAlbumArt.setImageDrawable(selected_item.getAlbumArt());
        txtCurrTitle.setText(selected_item.getTitle());
        txtCurrArtist.setText(selected_item.getArtist());

        try {
            mediaPlayer.setDataSource(context, uriCurrMusic);
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
        btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
    }


    public void permiction() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            loadMusic();
        }
    }

    private void loadMusic() {
        Drawable album_cover = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_contact_default);
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

                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3", "").replace("wav", "");
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST));
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                songlist.add(new Tab3ListViewItem(album_cover, title, artist, uri));
            }
            cursor.close();
        }

        adapter.setList(songlist);

    }

    public void playAndStop(View v) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            mediaPlayer.start();
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
        }
    }


    public void next(View v) {
        if(position != songlist.size()-1)
            position +=1;
        else
            position = 0;
        PlaySound(position);
    }

    public void prev(View v) {
        if(position != 0)
            position -= 1;
        else
            position = songlist.size()-1;
        PlaySound(position);
    }

}
