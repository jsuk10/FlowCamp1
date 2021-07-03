package com.example.apps;

public class Tab3MusicClass {
    private String name;
    private String uri;


    public Tab3MusicClass(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return name;
    }


    public String getUri() {
        return uri;
    }
}
