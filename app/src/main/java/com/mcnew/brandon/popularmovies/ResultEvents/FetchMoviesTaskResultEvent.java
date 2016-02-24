package com.mcnew.brandon.popularmovies.ResultEvents;

import com.mcnew.brandon.popularmovies.data.MovieObject;

import java.util.ArrayList;

/**
 * Created by Brandon on 1/21/2016.
 */
public class FetchMoviesTaskResultEvent {

    private ArrayList<MovieObject> result;

    public FetchMoviesTaskResultEvent(ArrayList<MovieObject> result) {
        this.result = result;
    }

    public ArrayList<MovieObject> getResult() {
        return result;
    }
}
