package com.example.apps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab3ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Tab3ListViewItem> listViewItemList = new ArrayList<Tab3ListViewItem>();

    // ListViewAdapter의 생성자
    public Tab3ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_tab3, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView albumCoverImageView = (ImageView) convertView.findViewById(R.id.imageAlbumCover);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textSongName);
        TextView artistTextView = (TextView) convertView.findViewById(R.id.textArtist);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Tab3ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //albumCoverImageView.setImageBitmap(listViewItem.getAlbumArt());
        albumCoverImageView.setImageDrawable(listViewItem.getAlbumArt());
        titleTextView.setText(listViewItem.getTitle());
        artistTextView.setText(listViewItem.getArtist());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void setList(ArrayList<Tab3ListViewItem> list){
        listViewItemList = list;
    }

}