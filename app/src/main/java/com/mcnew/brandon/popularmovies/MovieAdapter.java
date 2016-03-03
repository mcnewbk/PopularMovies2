package com.mcnew.brandon.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcnew.brandon.popularmovies.data.MovieObject;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 1/17/2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieObject>{

    private static final int VIEW_TYPE_COUNT = 1;
    private static Context mContext;
    private ArrayList<MovieObject> moviesArray;

    public static class ViewHolder{
        public final ImageView posterView;

        public ViewHolder(View view){
            posterView = (ImageView) view.findViewById(R.id.poster_imageview);
        }
    }

    public MovieAdapter(Activity activity, Context context, ArrayList<MovieObject> movies){
        super(context, 0, movies);
        this.moviesArray = movies;
        mContext = context;
    }

    public void setMoviesArray(ArrayList<MovieObject> movies){
        clear();
        if(movies != null){
            addAll(movies);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieObject movie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.movie_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);

        String baseURL = "http://image.tmdb.org/t/p/w780/";
        String moviePoster = baseURL + movie.getMovieURL();
        Picasso.with(mContext).load(moviePoster).into(viewHolder.posterView);

        return convertView;
    }

    @Override
    public int getCount() {
        return this.moviesArray.size();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

}
