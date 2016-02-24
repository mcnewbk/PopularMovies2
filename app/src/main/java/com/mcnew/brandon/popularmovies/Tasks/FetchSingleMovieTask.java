package com.mcnew.brandon.popularmovies.Tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;

import com.mcnew.brandon.popularmovies.BuildConfig;
import com.mcnew.brandon.popularmovies.MyOtto;
import com.mcnew.brandon.popularmovies.data.MovieObject;
import com.mcnew.brandon.popularmovies.ResultEvents.FetchSingleMovieTaskResultEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brandon on 1/17/2016.
 */
public class FetchSingleMovieTask extends AsyncTask<String, Void, MovieObject> {

    private final Context mContext;

    public FetchSingleMovieTask(Context context){
        mContext = context;
    }

    private MovieObject getMovieDataFromJson(String movieJsonString)
            throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";

        final String OWM_URL = "poster_path";
        final String OWM_TITLE = "title";
        final String OWM_YEAR = "release_date";
        final String OWM_LENGTH = "runtime";
        final String OWM_RATING = "vote_average";
        final String OWM_DESC = "overview";
        final String OWM_MOIVE_ID = "id";

        try {
            JSONObject movieJson = new JSONObject(movieJsonString);

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            String url = movieJson.getString(OWM_URL);
            String title = movieJson.getString(OWM_TITLE);
            String year = movieJson.getString(OWM_YEAR);
            String rating = movieJson.getString(OWM_RATING);
            String desc = movieJson.getString(OWM_DESC);
            String id = movieJson.getString(OWM_MOIVE_ID);
            MovieObject movieObj = new MovieObject(url, title, year, "121 mins", rating, desc, id);
            return movieObj;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected MovieObject doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        String movieQuery = params[0];

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonString = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String MOVIE_BASE_URL =
                    "https://api.themoviedb.org/3/movie/" + params[0];
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonString = buffer.toString();
            return getMovieDataFromJson(movieJsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieObject movie) {
        MyOtto.getInstance().post(new FetchSingleMovieTaskResultEvent(movie));
    }
}
