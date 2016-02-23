package com.mcnew.brandon.popularmovies;

import com.mcnew.brandon.popularmovies.data.MovieObject;

import java.util.ArrayList;

/**
 * Created by Brandon on 1/21/2016.
 */
public class FetchSingleMovieTaskResultEvent {

    private MovieObject result;

    public FetchSingleMovieTaskResultEvent(MovieObject result) {
        this.result = result;
    }

    public MovieObject getResult() {
        return result;
    }
}
