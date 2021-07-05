package com.example.apps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Tab3ListViewItem {
    private Bitmap albumartBitmap;
    private Drawable albumartDrawable;
    private String titleStr ;
    private String artistStr ;
    private String uriStr ;

    public Tab3ListViewItem() {

    }
    public Tab3ListViewItem(Bitmap album_art, String name, String artist, String uri) {
        this.albumartBitmap = album_art;
        this.titleStr = name;
        this.artistStr = artist;
        this.uriStr = uri;
    }

    public Tab3ListViewItem(Drawable album_art, String name, String artist, String uri) {
        this.albumartDrawable = album_art;
        this.titleStr = name;
        this.artistStr = artist;
        this.uriStr = uri;
    }

    public void setAlbumArt(Bitmap album_art) {
        albumartBitmap = album_art ;
    }
    public void setAlbumArt(Drawable album_art) {
        albumartDrawable = album_art ;
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

    //public Bitmap getAlbumArt() { return this.albumartBitmap ; }
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