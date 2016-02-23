package com.mcnew.brandon.popularmovies;

import com.squareup.otto.Bus;

/**
 * Created by Brandon on 1/21/2016.
 */
public class MyOtto {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
}
