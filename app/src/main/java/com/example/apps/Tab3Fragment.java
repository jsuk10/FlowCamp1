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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
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
    private Integer Maxduration = 999999;
    private Integer stopTime = 0;
    private Tab3ListViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton btnPlayAndStop;


    private Uri uriCurrMusic;
    private ImageView imgCurrAlbumArt;
    private TextView txtCurrTitle;
    private TextView txtCurrArtist;
    private TextView playTime;
    private TextView endTime;
    private SeekBar seekbar;

    private Handler handler = new Handler();

    private Thread playThread,nextThread,beforeThread;

    public Tab3Fragment() {
        // Required empty public constructor
    }

    public static Tab3Fragment newInstance() {
        Tab3Fragment tab3_fragment = new Tab3Fragment();
        return tab3_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("hello", "Hello");
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

    public void init() {
        songlist = new ArrayList<>();
        adapter = new Tab3ListViewAdapter();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion (MediaPlayer mp){
                seekbar.setProgress(0);
//                next(view);
            }
        });

        listView = view.findViewById(R.id.tab3_listView);
        listView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.tab3_swipeRefresh);
        btnPlayAndStop = view.findViewById(R.id.tab3_playButton);

        imgCurrAlbumArt = (ImageView) view.findViewById(R.id.tab3_imgAlbumArt);
        txtCurrTitle = (TextView) view.findViewById(R.id.tab3_txtTitle);
        txtCurrArtist = (TextView) view.findViewById(R.id.tab3_txtArtist);

        seekbar = view.findViewById(R.id.tab3_seekBar);
        playTime = (TextView) view.findViewById(R.id.tab3_durationPlay);
        endTime = (TextView) view.findViewById(R.id.tab3_durationEnd);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser)
                    mediaPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int currentPostion = mediaPlayer.getCurrentPosition() / 1000;
                            playTime.setText(formattendTime(currentPostion));
                                seekbar.setProgress(currentPostion);
//                            if (currentPostion >= Maxduration && isTracking == false && mediaPlayer.isPlaying()) {
//                                seekbar.setProgress(0);
//                                next(view);
//                            }
                        }
                        handler.postDelayed(this, 1000);
                    }
                });
            }
        }).start();


        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.pause();
    }

    private String formattendTime(int currentPostion) {
        String seconds = String.valueOf(currentPostion % 60);
        String minutes = String.valueOf(currentPostion / 60);
        if (seconds.length() == 1)
            return minutes + ":0" + seconds;
        else
            return minutes + ":" + seconds;
    }

    public void addButtonEvent() {
        view.findViewById(R.id.tab3_playButton).setOnClickListener(v -> playAndStop(view));
        view.findViewById(R.id.tab3_prevButton).setOnClickListener(v -> prev(view));
        view.findViewById(R.id.tab3_nextButton).setOnClickListener(v -> next(view));
    }

    public void setListCilck() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View views, int position, long id) {
                Tab3Fragment.this.position = position;
                stopTime = 0;
                PlaySound(position);
            }
        });
    }

    public void PlaySound(Integer position) {
        this.position = position;
        Tab3ListViewItem selected_item = (Tab3ListViewItem) songlist.get(position);

        mediaPlayer.reset();
        uriCurrMusic = Uri.parse(selected_item.getUri());

        imgCurrAlbumArt.setImageURI(selected_item.getAlbumArt());
        txtCurrTitle.setText(selected_item.getTitle());
        txtCurrArtist.setText(selected_item.getArtist());

        seekbar.setMax(Integer.parseInt(selected_item.getDuration()) / 1000);
        playTime.setText(formattendTime(mediaPlayer.getDuration() / 1000));
        Maxduration = Integer.parseInt(selected_item.getDuration()) / 1000;

        endTime.setText(formattendTime(Integer.parseInt(selected_item.getDuration()) / 1000));

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
        mediaPlayer.seekTo(stopTime);
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
        //Drawable album_art = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_contact_default);
        songlist.clear();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST));
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                long albumId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM_ID));
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri sAlbumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                songlist.add(new Tab3ListViewItem(sAlbumArtUri, title, artist, uri, duration));

            }
            cursor.close();
        }

        adapter.setList(songlist);

    }

    public void playAndStop(View v) {
        if (mediaPlayer.isPlaying()) {
            stopTime = mediaPlayer.getDuration();
            mediaPlayer.pause();
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_play);
        } else {
            PlaySound(position);
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
        }
    }


    public void next(View v) {
        stopTime=0;
        if (position != songlist.size() - 1)
            position += 1;
        else
            position = 0;
        PlaySound(position);
    }

    public void prev(View v) {
        stopTime =0;
        if (position != 0)
            position -= 1;
        else
            position = songlist.size() - 1;
        PlaySound(position);
    }

}
