package com.example.user.movieapp1;

/**
 * Created by user on 11/26/2016.
 */
public class Movie {
    private String poster_url;
    private int id;
    private String title;
    private String date;
    private double vote;
    private String overview;
    static String state;
    private String movie;

    public String getPoster_url() {
        return poster_url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getVote() {
        return vote;
    }

    public String getOverview() {
        return overview;
    }

    public String getMovie(){ return movie;}

    public void setMovie(String movie){ this.movie = movie; }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String titel) {
        this.title = titel;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
