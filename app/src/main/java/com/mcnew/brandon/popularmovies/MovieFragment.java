package com.mcnew.brandon.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mcnew.brandon.popularmovies.Tasks.FetchMoviesTask;
import com.mcnew.brandon.popularmovies.data.MovieContract;
import com.mcnew.brandon.popularmovies.data.MovieObject;
import com.mcnew.brandon.popularmovies.ResultEvents.FetchMoviesTaskResultEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieAdapter mMovieAdapter;

    public interface Callback {
        public void onItemSelected(String id);
    }

    private GridView mGridView;
    private ArrayList<MovieObject> movies;
    private View root;

    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_URL,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_YEAR,
            MovieContract.MovieEntry.COLUMN_LENGTH,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DESC,
            MovieContract.MovieEntry.COLUMN_ID
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_URL = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_YEAR = 3;
    static final int COL_MOVIE_LENGTH = 4;
    static final int COL_MOVIE_RATING = 5;
    static final int COL_MOVIE_DESC = 6;
    static final int COL_MOVIE_MOVIEID = 7;


    public MovieFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        if(mMovieAdapter != null && movies != null){
            mMovieAdapter.setMoviesArray(movies);
        }
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        root = rootView;
        MyOtto.getInstance().register(this);
        mGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        setupAdapter();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Subscribe
    public void onAsyncTaskResult(FetchMoviesTaskResultEvent event) {
        movies = new ArrayList<MovieObject>();
        movies = event.getResult();

    }

    public void setupAdapter(){

        updateMovies();
        boolean isOnline = NetworkHelper.isOnline(getActivity().getBaseContext());
        if(!isOnline){
            root.findViewById(R.id.networkProblems).setVisibility(View.VISIBLE);
        }else{
            root.findViewById(R.id.networkProblems).setVisibility(View.GONE);
        }
        if(isOnline && movies != null){
            mMovieAdapter = new MovieAdapter(getActivity(), getActivity(), movies); //this will need to get movies
            mGridView.setAdapter(mMovieAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //what happens when I click an item
                }
            });

            SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                    SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                              String key) {
                            if(movies != null){
                                mMovieAdapter.setMoviesArray(movies);
                            }
                        }
                    };

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieObject movieObject = (MovieObject) parent.getItemAtPosition(position);
                    ((Callback) getActivity()).onItemSelected(movieObject.getMovieID());
                }
            });
        }
    }

    void updateMovies(){
        FetchMoviesTask moviesTask = new FetchMoviesTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String queryBy = prefs.getString(getActivity().getString(R.string.pref_sort_key),
                getActivity().getString(R.string.pref_sort_default));
        try {
            moviesTask.execute(queryBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = getPreferredSort(getActivity().getBaseContext());
        Uri weatherForLocationUri = MovieContract.MovieEntry.buildMovieUriWithSort(sortOrder);

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                MOVIE_COLUMNS,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_RATING + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //mMovieAdapter.swapCursor(data);
        if(mMovieAdapter != null && movies != null) {
            mMovieAdapter.setMoviesArray(movies);
        }else{
            setupAdapter();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // mMovieAdapter.swapCursor(null);
        if(mMovieAdapter != null && movies != null) {
            mMovieAdapter.setMoviesArray(movies);
        } else{
            setupAdapter();
        }
    }

    public String getPreferredSort(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String queryBy = prefs.getString(getActivity().getString(R.string.pref_sort_key),
                getActivity().getString(R.string.pref_sort_default));
        return queryBy;
    }
}
