package com.example.apps;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
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
    private ArrayList<Tab3ListViewItem> musicList;
    private MediaPlayer mediaPlayer;
    private Context context;
    private Integer position;
    private Tab3ListViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton btnPlayAndStop;


    private Uri uriCurrMusic;
    private ImageView imgCurrAlbumArt;
    private TextView txtCurrTitle;
    private TextView txtCurrArtist;
    private Tab3ListViewItem itemCurrMusic;

    public Tab3Fragment() {
        // Required empty public constructor
    }

    public static Tab3Fragment newInstance() {
        Tab3Fragment tab3_fragment = new Tab3Fragment();
        Log.e("hello","New Instance");
        return tab3_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        musicList = new ArrayList<>();
        adapter = new Tab3ListViewAdapter();
        mediaPlayer = new MediaPlayer();
        position = 0;
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
        permissionCheck();
        return view;
    }

    private void setMoveText() {
        txtCurrTitle.setSelected(true);
    }

    public void init(){
        listView = view.findViewById(R.id.tab3_listView);
        listView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.tab3_swipeRefresh);
        btnPlayAndStop = view.findViewById(R.id.tab3_playButton);

        imgCurrAlbumArt = (ImageView) view.findViewById(R.id.tab3_imgAlbumArt);
        txtCurrTitle = (TextView) view.findViewById(R.id.tab3_txtTitle);
        txtCurrArtist = (TextView) view.findViewById(R.id.tab3_txtArtist);

        if(itemCurrMusic != null) {
            imgCurrAlbumArt.setImageURI(itemCurrMusic.getAlbumArt());
            txtCurrTitle.setText(itemCurrMusic.getTitle());
            txtCurrArtist.setText(itemCurrMusic.getArtist());
        }
        if(mediaPlayer.isPlaying()) {
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_play);
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
                playMusic(position);
            }
        });
    }

    public void playMusic(Integer position) {

        mediaPlayer.reset();

        itemCurrMusic = musicList.get(position);
        uriCurrMusic = Uri.parse(itemCurrMusic.getUri());
        imgCurrAlbumArt.setImageURI(itemCurrMusic.getAlbumArt());
        txtCurrTitle.setText(itemCurrMusic.getTitle());
        txtCurrArtist.setText(itemCurrMusic.getArtist());

        try {
            mediaPlayer.setDataSource(context, uriCurrMusic);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "NullPointerException", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "IllegalStateException", Toast.LENGTH_SHORT).show();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "NullPointerException", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "IllegalStateException", Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.start();
        btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
    }


    public void permissionCheck() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            loadMusic();
        }
    }

    private void loadMusic() {
        musicList.clear();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.TITLE + " ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST));
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                long albumId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM_ID));
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri sAlbumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                musicList.add(new Tab3ListViewItem(sAlbumArtUri, title, artist, uri));

            }
            cursor.close();
        }

        adapter.setList(musicList);

    }

    public void playAndStop(View v) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            playMusic(position);
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
        }
    }


    public void next(View v) {
        if(position != musicList.size()-1)
            position +=1;
        else
            position = 0;
        playMusic(position);
    }

    public void prev(View v) {
        if(position != 0)
            position -= 1;
        else
            position = musicList.size()-1;
        playMusic(position);
    }

}
