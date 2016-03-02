package com.mcnew.brandon.popularmovies.Adapters;

/**
 * Created by Brandon on 3/1/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcnew.brandon.popularmovies.R;
import com.mcnew.brandon.popularmovies.data.MovieObject;
import com.mcnew.brandon.popularmovies.data.TrailerObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<TrailerObject> {

    private static final int VIEW_TYPE_COUNT = 1;
    private static Context mContext;
    private ArrayList<TrailerObject> trailersArray;

    public static class ViewHolder {
        public final ImageView posterView;
        public final TextView name;

        public ViewHolder(View view) {
            posterView = (ImageView) view.findViewById(R.id.poster_imageview);
            name = (TextView) view.findViewById(R.id.trailerName);
        }
    }

    public TrailerAdapter(Activity activity, Context context, ArrayList<TrailerObject> trailers) {
        super(context, 0, trailers);
        this.trailersArray = trailers;
        mContext = context;
    }

    public void setTrailersArray(ArrayList<TrailerObject> trailers) {
        clear();
        if (trailers != null) {
            addAll(trailers);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TrailerObject trailer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.trailer_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.name.setText("Trailer " + Integer.toString(position + 1));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoId = trailer.getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                intent.putExtra("VIDEO_ID", videoId);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return this.trailersArray.size();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}

