package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public String mediaUrl;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        if(!jsonObject.isNull("extended_entities")){
            JSONObject extendedEntities = jsonObject.getJSONObject("extended_entities");
            JSONArray jsonArray = extendedEntities.getJSONArray("media");
            JSONObject media = jsonArray.getJSONObject(0);
            tweet.mediaUrl = media.getString("media_url_https");

        }
        else{
            tweet.mediaUrl ="";
        }

        return  tweet;
    }
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i=0; i <jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;

    }

}
