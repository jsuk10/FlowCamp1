package com.example.apps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;

import static com.example.apps.MainActivity.musicFile;

public class Tab3Fragment extends Fragment {
    private View view;

    RecyclerView recyclerView;
//    private ArrayList<Tab3ListViewItem> songlist;
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
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        context = container.getContext();

        if(!(musicFile.size()<1)) {
            adapter = new Tab3ListViewAdapter(getContext(), musicFile);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        init();
        addButtonEvent();
        setMoveText();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe 시 진행할 동작 */
//                loadMusic();
                /* 업데이트가 끝났음을 알림 */
                swipeRefreshLayout.setRefreshing(false);
            }
        });
//        permissionCheck();
        return view;
    }

    private void setMoveText() {
        txtCurrTitle.setSelected(true);
    }

    public void init() {
        adapter = new Tab3ListViewAdapter();
        mediaPlayer = new MediaPlayer();
        btnPlayAndStop = view.findViewById(R.id.tab3_playButton);

        imgCurrAlbumArt = (ImageView) view.findViewById(R.id.tab3_imgAlbumArt);
        txtCurrTitle = (TextView) view.findViewById(R.id.tab3_txtTitle);
        txtCurrArtist = (TextView) view.findViewById(R.id.tab3_txtArtist);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        swipeRefreshLayout = view.findViewById(R.id.tab3_swipeRefresh);
        mediaPlayer.pause();
    }

    public void addButtonEvent() {
        view.findViewById(R.id.tab3_playButton).setOnClickListener(v -> playAndStop(view));
        view.findViewById(R.id.tab3_prevButton).setOnClickListener(v -> prev(view));
        view.findViewById(R.id.tab3_nextButton).setOnClickListener(v -> next(view));
    }


    public void PlaySound(Integer position) {
        Tab3ListViewItem selected_item = (Tab3ListViewItem) musicFile.get(position);

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

    public void playAndStop(View v) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_play);
        } else {
            PlaySound(position);
            btnPlayAndStop.setImageResource(android.R.drawable.ic_media_pause);
        }
    }


    public void next(View v) {
        if (position != musicFile.size() - 1)
            position += 1;
        else
            position = 0;
        PlaySound(position);
    }

    public void prev(View v) {
        if (position != 0)
            position -= 1;
        else
            position = musicFile.size() - 1;
        PlaySound(position);
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 130, 130, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
}
