package com.mcnew.brandon.popularmovies.ResultEvents;

import com.mcnew.brandon.popularmovies.data.MovieObject;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/2/2016.
 */
public class FetchFavoriteMoviesTaskResultEvent {

    private ArrayList<MovieObject> result;

    public FetchFavoriteMoviesTaskResultEvent(ArrayList<MovieObject> result) {
        this.result = result;
    }

    public ArrayList<MovieObject> getResult() {
        return result;
    }
}
