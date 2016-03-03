package com.mcnew.brandon.popularmovies;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mcnew.brandon.popularmovies.Tasks.DetailFragment;
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Intent intent = this.getIntent();
            String id = "";
            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
                id = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
            arguments.putString(DetailFragment.MOVIE_ID, id);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
