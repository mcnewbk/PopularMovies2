package com.mcnew.brandon.popularmovies.ResultEvents;
import com.mcnew.brandon.popularmovies.data.TrailerObject;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/23/2016.
 */
public class FetchTrailerTaskResultEvent {

    private ArrayList<TrailerObject> result;

    public FetchTrailerTaskResultEvent(ArrayList<TrailerObject> result) {
        this.result = result;
    }

    public ArrayList<TrailerObject> getResult() {
        return result;
    }
}
