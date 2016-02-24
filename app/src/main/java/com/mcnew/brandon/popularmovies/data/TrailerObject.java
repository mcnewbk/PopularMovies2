package com.mcnew.brandon.popularmovies.data;

/**
 * Created by Brandon on 2/23/2016.
 */

public class TrailerObject{
    String trailerID;
    String iso;
    String key;
    String name;
    String site;
    String size;
    String type;

    public TrailerObject(String id, String iso, String key, String name, String site, String size, String type){
        this.trailerID = id;
        this.iso = iso;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getTrailerID(){
        return trailerID;
    }

    public String getIso(){
        return iso;
    }

    public String getKey(){
        return key;
    }

    public String getName(){
        return name;
    }

    public String getSite(){
        return site;
    }

    public String getSize(){
        return size;
    }
    public String getType(){
        return type;
    }

}
