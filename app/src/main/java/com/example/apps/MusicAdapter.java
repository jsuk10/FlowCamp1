package com.example.apps;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> {
    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

    // ListViewAdapter의 생성자
    public MusicAdapter() {

    }

    public MusicAdapter(Context mContext, ArrayList<MusicFiles> mFile) {
        this.mFiles = mFile;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyVieHolder holder, int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());
        byte[] image = getAlbumArt(mFiles.get(position).getPath());
        if(image!=null)
            Glide.with(mContext).asBitmap().load(image).into(holder.album_art);
        else
            Glide.with(mContext).asBitmap().load(R.drawable.ic_contact_default).into(holder.album_art);

    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyVieHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art;
        public MyVieHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
        }
    }
    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
