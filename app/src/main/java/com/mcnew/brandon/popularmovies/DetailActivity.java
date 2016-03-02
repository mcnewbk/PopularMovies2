package com.mcnew.brandon.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcnew.brandon.popularmovies.Adapters.ReviewAdapter;
import com.mcnew.brandon.popularmovies.Adapters.TrailerAdapter;
import com.mcnew.brandon.popularmovies.Tasks.FetchReviewTask;
import com.mcnew.brandon.popularmovies.Tasks.FetchSingleMovieTask;
import com.mcnew.brandon.popularmovies.Tasks.FetchTrailerTask;
import com.mcnew.brandon.popularmovies.data.MovieObject;
import com.mcnew.brandon.popularmovies.ResultEvents.FetchSingleMovieTaskResultEvent;
import com.mcnew.brandon.popularmovies.ResultEvents.FetchTrailerTaskResultEvent;
import com.mcnew.brandon.popularmovies.ResultEvents.FetchReviewTaskResultEvent;
import com.mcnew.brandon.popularmovies.data.ReviewObject;
import com.mcnew.brandon.popularmovies.data.TrailerObject;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ImageView poster;
    private TextView title;
    private TextView year;
    private TextView rating;
    private TextView desc;
    private MovieObject movie;
    private ArrayList<TrailerObject> trailers;
    private ArrayList<ReviewObject> reviews;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private ListView trailersListView;
    private ListView reviewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        MyOtto.getInstance().register(this);

        trailersListView = (ListView)findViewById(R.id.trailers);
        reviewListView = (ListView)findViewById(R.id.reviews);

        poster = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        rating = (TextView) findViewById(R.id.rating);
        desc = (TextView) findViewById(R.id.desc);

        Intent intent = this.getIntent();
        String id = "";
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            id = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        FetchSingleMovieTask moviesTask = new FetchSingleMovieTask(this);
        FetchTrailerTask trailerTask = new FetchTrailerTask(this);
        FetchReviewTask reviewTask = new FetchReviewTask(this);
        try {
            moviesTask.execute(id);
            trailerTask.execute(id);
            reviewTask.execute(id);
        } catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    protected void onDestroy() {
        MyOtto.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onAsyncTaskResult(FetchSingleMovieTaskResultEvent event) {
        movie = event.getResult();
        String baseURL = "http://image.tmdb.org/t/p/w500/";
        String moviePoster = baseURL + movie.getMovieURL();
        Picasso.with(this).load(moviePoster).into(poster);
        title.setText(movie.getTitle());
        year.setText(movie.getYear().substring(0, 4));
        rating.setText(movie.getRating() + "/10");
        desc.setText(movie.getDescription());
    }

    @Subscribe
    public void onAsyncTaskResult(FetchReviewTaskResultEvent event) {
        reviews = event.getResult();
        setupReviewAdapter();
    }

    @Subscribe
    public void onAsyncTaskResult(FetchTrailerTaskResultEvent event) {
        trailers = event.getResult();
        setupTrailerAdapter();
    }

    public void setupTrailerAdapter(){
        boolean isOnline = NetworkHelper.isOnline(getBaseContext());
        if(!isOnline){
            findViewById(R.id.noTrailers).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.noTrailers).setVisibility(View.GONE);
        }
        if(isOnline && trailers != null){
            mTrailerAdapter = new TrailerAdapter(this, this, trailers); //this will need to get movies
            //mTrailerAdapter.setTrailersArray(trailers);
            trailersListView.setAdapter(mTrailerAdapter);
        }
    }

    public void setupReviewAdapter(){
        boolean isOnline = NetworkHelper.isOnline(getBaseContext());
        if(!isOnline){
            findViewById(R.id.noReviews).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.noReviews).setVisibility(View.GONE);
        }
        if(isOnline && reviews != null){
            mReviewAdapter = new ReviewAdapter(this, this, reviews); //this will need to get movies
            //mReviewAdapter.setReviewsArray(reviews);
            reviewListView.setAdapter(mReviewAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
