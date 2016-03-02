package com.mcnew.brandon.popularmovies.Adapters;

/**
 * Created by Brandon on 3/1/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcnew.brandon.popularmovies.R;
import com.mcnew.brandon.popularmovies.data.MovieObject;
import com.mcnew.brandon.popularmovies.data.ReviewObject;
import com.mcnew.brandon.popularmovies.data.TrailerObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<ReviewObject> {

    private static final int VIEW_TYPE_COUNT = 1;
    private static Context mContext;
    private ArrayList<ReviewObject> reviewsArray;

    public static class ViewHolder {
        public final TextView author;
        public final TextView review;

        public ViewHolder(View view) {
            author = (TextView) view.findViewById(R.id.author);
            review = (TextView) view.findViewById(R.id.review_desc);
        }
    }

    public ReviewAdapter(Activity activity, Context context, ArrayList<ReviewObject> reviews) {
        super(context, 0, reviews);
        this.reviewsArray = reviews;
        mContext = context;
    }

    public void setReviewsArray(ArrayList<ReviewObject> reviews) {
        clear();
        if (reviews != null) {
            addAll(reviews);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewObject review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.review_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.author.setText(review.getAuthor() + " wrote: ");
        viewHolder.review.setText(review.getContent());

        return convertView;
    }

    @Override
    public int getCount() {
        return this.reviewsArray.size();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}

