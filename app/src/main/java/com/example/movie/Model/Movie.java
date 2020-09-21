package com.example.movie.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {
    public String title;
    public String poster;
    public String imdbid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbid() {
        return imdbid;
    }

    public void setImdbid(String imdbid) {
        this.imdbid = imdbid;
    }

    public Movie(String title, String poster, String imdbid) {
        this.title = title;
        this.poster = poster;
        this.imdbid = imdbid;
    }

    public Movie(){
        this.title = title;
        this.poster = poster;
        this.imdbid = imdbid;
    }
}
