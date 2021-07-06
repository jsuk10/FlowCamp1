package com.example.apps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Tab3ListViewItem {
    private Uri albumartUri;
    private String titleStr ;
    private String artistStr ;
    private String uriStr ;

    public Tab3ListViewItem() {

    }
    public Tab3ListViewItem(Uri album_art, String name, String artist, String uri) {
        this.albumartUri = album_art;
        this.titleStr = name;
        this.artistStr = artist;
        this.uriStr = uri;
    }


    public void setAlbumArt(Uri album_art) {
        albumartUri = album_art ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setArtist(String artist) {
        artistStr = artist ;
    }
    public void setUri(String uri) {
        uriStr = uri ;
    }

    public Uri getAlbumArt() { return this.albumartUri ; }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getArtist() {
        return this.artistStr ;
    }
    public String getUri() {
        return this.uriStr ;
    }

    @Override
    public String toString() {
        return titleStr;
    }
}