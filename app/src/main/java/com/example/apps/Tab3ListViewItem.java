package com.example.apps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Tab3ListViewItem {
    private Bitmap albumartBitmap;
    private Drawable albumartDrawable;
    private String titleStr;
    private String artistStr;
    private String uriStr;
    private String album;
    private String title;
    private String duration;
    private String path;
    private String artist;

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() { return album; }

    public String getDuration() { return duration; }

    public String getPath() { return path; }

    public Tab3ListViewItem() { }

    public Tab3ListViewItem(String path, String title, String artist, String album, String duration) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }


    public Tab3ListViewItem(Drawable album_art, String name, String artist, String uri) {
        this.albumartDrawable = album_art;
        this.titleStr = name;
        this.artistStr = artist;
        this.uriStr = uri;
    }

    public void setAlbumArt(Bitmap album_art) {
        albumartBitmap = album_art;
    }

    public void setAlbumArt(Drawable album_art) {
        albumartDrawable = album_art;
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setArtist(String artist) {
        artistStr = artist;
    }

    public void setUri(String uri) {
        uriStr = uri;
    }

    //아래 다 날려

    //public Bitmap getAlbumArt() { return this.albumartBitmap ; }
    public Drawable getAlbumArt() {
        return this.albumartDrawable;
    }

    public String getUri() {
        return this.path;
    }

    @Override
    public String toString() {
        return title;
    }
}