package com.mcnew.brandon.popularmovies.data;

/**
 * Created by Brandon on 1/19/2016.
 */

public class MovieObject{
    String movieURL;
    String title;
    String year;
    String length;
    String rating;
    String description;
    String movieID;

    public MovieObject(String url, String title, String year, String length, String rating, String desc, String id){
        this.movieURL = url;
        this.title = title;
        this.year = year;
        this.length = length;
        this.rating = rating;
        this.description = desc;
        this.movieID = id;
    }

    public String getMovieID(){
        return movieID;
    }

    public String getMovieURL(){
        return movieURL;
    }

    public String getTitle(){
        return title;
    }

    public String getYear(){
        return year;
    }

    public String getLength(){
        return length;
    }

    public String getRating(){
        return rating;
    }
    public String getDescription(){
        return description;
    }

}
