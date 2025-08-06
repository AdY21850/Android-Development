package com.example.watchit;

public class RecommendationModel {
    private String posterUrl; // URL for the movie poster
    private String movieurl;
    private String title;
    private String genre;
    private String year;
    private String rating;
    private String summary;

    public RecommendationModel(String title,String posterUrl ,String movieurl, String genre, String year, String rating, String summary) {
        this.posterUrl = posterUrl;
        this.movieurl=movieurl;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.summary = summary;
    }

    public String getPosterUrl() { return posterUrl; }
    public String getmovieurl() { return movieurl; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getYear() { return year; }
    public String getRating() { return rating; }
    public String getSummary() { return summary; }
}
