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

public class Tab3ListViewAdapter extends RecyclerView.Adapter<Tab3ListViewAdapter.MyVieHolder>{
    private Context mContext;
    private ArrayList<Tab3ListViewItem> mFiles;

    // ListViewAdapter의 생성자
    public Tab3ListViewAdapter() {

    }
    public Tab3ListViewAdapter(Context mContext, ArrayList<Tab3ListViewItem> mFile) {
        this.mFiles = mFile;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Tab3ListViewAdapter.MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new Tab3ListViewAdapter.MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Tab3ListViewAdapter.MyVieHolder holder, int position) {
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
            file_name = itemView.findViewById(R.id.textSongName);
            album_art = itemView.findViewById(R.id.imageView1);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Tab3Fragment.instance.position = position;
//                    Tab3Fragment.instance.PlaySound(position);
//                }
//            });
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }
    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
//    @Override
//    public Object getItem(int position) {
//        return mFiles.get(position);
//    }

    public void setList(ArrayList<Tab3ListViewItem> list){
        mFiles = list;
    }
}