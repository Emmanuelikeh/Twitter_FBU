package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;


    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTweetBinding itemTweetBinding = ItemTweetBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(itemTweetBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        ImageView ivProfileImage;
//        TextView tvBody;
//        TextView tvScreenName;
//        ImageView ivMediaUrl;
//        TextView tvTimeStamp;
//        TextView tvName;

        ItemTweetBinding itemTweetBinding;

        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


        public ViewHolder(@NonNull ItemTweetBinding itemTweetBinding) {
            super(itemTweetBinding.getRoot());
            this.itemTweetBinding = itemTweetBinding;
//
//            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
//            tvBody = itemView.findViewById(R.id.tvBody);
//            tvScreenName = itemView.findViewById(R.id.tvScreenName);
//            ivMediaUrl = itemView.findViewById(R.id.ivMediaUrl);
//            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
//            tvName = itemView.findViewById(R.id.tvName);


        }

        public String getRelativeTimeAgo(String rawJsonDate) {
            //function gets the created at string and modifiies it so it would be displayed
            // in a specific way to the user

            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            try {
                long time = sf.parse(rawJsonDate).getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "a minute ago";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + " m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + " h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + " d";
                }
            } catch (ParseException e) {
                Log.i("TweetAdapter", "getRelativeTimeAgo failed");
                e.printStackTrace();
            }
            return "";
        }



        public void bind(Tweet tweet) {
            itemTweetBinding.tvBody.setText(tweet.body);
            itemTweetBinding.tvTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));
            itemTweetBinding.tvScreenName.setText("@"+ tweet.user.screenName);
            itemTweetBinding.tvName.setText(tweet.user.name);
            Glide.with(context).load(tweet.user.profileImageUrl).into(itemTweetBinding.ivProfileImage);
//            tvBody.setText(tweet.body);
//            tvTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));
//            tvScreenName.setText("@"+tweet.user.screenName);
//            tvName.setText(tweet.user.name);


            // mediaUrl does not always have an image link, in other to handle this exception
            //the following algorithm was employed

            if(!tweet.mediaUrl.equals("")){
                Glide.with(context).load(tweet.mediaUrl).into(itemTweetBinding.ivMediaUrl);
            }
            else{
                itemTweetBinding.ivMediaUrl.setVisibility(View.GONE);
            }
        }
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


}
