package com.mcnew.brandon.popularmovies.Tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;

import com.mcnew.brandon.popularmovies.BuildConfig;
import com.mcnew.brandon.popularmovies.MyOtto;
import com.mcnew.brandon.popularmovies.ResultEvents.FetchTrailerTaskResultEvent;
import com.mcnew.brandon.popularmovies.data.TrailerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Brandon on 1/17/2016.
 */
public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<TrailerObject>> {

    private final Context mContext;

    public FetchTrailerTask(Context context){
        mContext = context;
    }

    private ArrayList<TrailerObject> getTrailerDataFromJson(String movieJsonString)
            throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";

        final String OWM_ID = "id";
        final String OWM_ISO = "iso_639_1";
        final String OWM_KEY= "key";
        final String OWM_NAME = "name";
        final String OWM_SITE = "site";
        final String OWM_SIZE = "size";
        final String OWM_TYPE = "type";

        try {
            JSONObject trailerJSON = new JSONObject(movieJsonString);
            JSONArray trailerArray = trailerJSON.getJSONArray(OWM_LIST);
            ArrayList<TrailerObject> cVVector = new ArrayList<TrailerObject>(trailerArray.length());

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);

                String id = trailer.getString(OWM_ID);
                String iso = trailer.getString(OWM_ISO);
                String key = trailer.getString(OWM_KEY);
                String name = trailer.getString(OWM_NAME);
                String site = trailer.getString(OWM_SITE);
                String size = trailer.getString(OWM_SIZE);
                String type = trailer.getString(OWM_TYPE);

//                ContentValues movieValues = new ContentValues();
//
//                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, url);
//                movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
//                movieValues.put(MovieContract.MovieEntry.COLUMN_YEAR, year);
//                movieValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, "121 mins");
//                movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
//                movieValues.put(MovieContract.MovieEntry.COLUMN_DESC, desc);
//                movieValues.put(MovieContract.MovieEntry.COLUMN_ID, id);
                TrailerObject trailerObj = new TrailerObject(id, iso, key, name, site, size, type);
                cVVector.add(trailerObj);
            }

            //int inserted = 0;
            // add to database

            return cVVector;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected ArrayList<TrailerObject> doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        String movieId = params[0];

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
                    "https://api.themoviedb.org/3/movie/" + params[0] + "/videos";
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
            return getTrailerDataFromJson(movieJsonString);
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
    protected void onPostExecute(ArrayList<TrailerObject> trailers) {
        MyOtto.getInstance().post(new FetchTrailerTaskResultEvent(trailers));
    }
}
