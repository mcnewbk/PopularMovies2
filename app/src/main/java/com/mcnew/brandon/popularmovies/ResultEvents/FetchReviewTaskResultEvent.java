package com.mcnew.brandon.popularmovies.ResultEvents;

import com.mcnew.brandon.popularmovies.data.ReviewObject;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/23/2016.
 */
public class FetchReviewTaskResultEvent {

    private ArrayList<ReviewObject> result;

    public FetchReviewTaskResultEvent(ArrayList<ReviewObject> result) {
        this.result = result;
    }

    public ArrayList<ReviewObject> getResult() {
    return result;
}
}
