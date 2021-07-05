package com.example.apps;

import android.graphics.drawable.Drawable;

public class Tab3ListViewItem {
    private Drawable albumartDrawable ;
    private String titleStr ;
    private String artistStr ;
    private String uriStr ;

    public Tab3ListViewItem() {

    }
    public Tab3ListViewItem(Drawable album_cover, String name, String artist, String uri) {
        this.albumartDrawable = album_cover;
        this.titleStr = name;
        this.artistStr = artist;
        this.uriStr = uri;
    }

    public void setAlbumArt(Drawable album_cover) {
        albumartDrawable = album_cover ;
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

    public Drawable getAlbumArt() { return this.albumartDrawable ; }
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