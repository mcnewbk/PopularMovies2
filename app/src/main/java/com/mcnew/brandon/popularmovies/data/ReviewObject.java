package com.mcnew.brandon.popularmovies.data;

/**
 * Created by Brandon on 2/23/2016.
 */
public class ReviewObject{
    String reviewID;
    String author;
    String content;
    String url;

    public ReviewObject(String reviewID, String author, String content, String url){
        this.reviewID = reviewID;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getReviewID(){
        return reviewID;
    }

    public String getAuthor(){
        return author;
    }

    public String getContent(){
        return content;
    }

    public String getUrl(){
        return url;
    }

}