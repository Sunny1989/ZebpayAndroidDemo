package com.zebpay.demo.sumeet.chawla.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.databinding.FeedItemBinding;
import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeed;
import com.zebpay.demo.sumeet.chawla.utility.Constants;
import com.zebpay.demo.sumeet.chawla.views.DetailActivity;
import com.zebpay.demo.sumeet.chawla.volley.MyVolley;

/**
 * Created by Sumeet on 26-07-2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.Feedholder> {

    private ObservableArrayList<ActivityFeed> activityFeeds;
    private Context context;
    private RecyclerView recyclerViewFeed;


    public FeedAdapter(RecyclerView recyclerViewFeed, ObservableArrayList<ActivityFeed> activityFeeds, Context context) {
        this.recyclerViewFeed = recyclerViewFeed;
        this.activityFeeds = activityFeeds;
        this.context = context;
    }


    @Override
    public FeedAdapter.Feedholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        return new Feedholder(v);
    }

    @Override
    public void onBindViewHolder(Feedholder holder, int position) {
        //Utils.getTimeInAgo()
        //And show image or first letter..
        final ActivityFeed activityFeed = activityFeeds.get(position);
        holder.feedItemBinding.setFeedItems(activityFeed);
        holder.feedItemBinding.setFeedAdapter(this);

        if (activityFeed.SourceImageUrl != null && !activityFeed.SourceImageUrl.isEmpty()) {
            holder.feedItemBinding.rivAvatar.setVisibility(View.VISIBLE);
            holder.feedItemBinding.ivAvatarName.setVisibility(View.GONE);
            holder.feedItemBinding.rivAvatar.setImageUrl(activityFeed.SourceImageUrl, MyVolley.getInstance(context).getImageLoader());
        } else {
            holder.feedItemBinding.rivAvatar.setVisibility(View.GONE);
            holder.feedItemBinding.ivAvatarName.setVisibility(View.VISIBLE);
            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(activityFeed.Name.toUpperCase().charAt(0)), Color.RED);
            holder.feedItemBinding.ivAvatarName.setImageDrawable(drawable);
        }

        //}

        holder.feedItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (activityFeeds == null) {
            return 0;
        } else {
            return activityFeeds.size();
        }
    }


    public class Feedholder extends RecyclerView.ViewHolder {
        public FeedItemBinding feedItemBinding;

        public Feedholder(View itemView) {
            super(itemView);
            feedItemBinding = DataBindingUtil.bind(itemView);
        }
    }

    public void openDetailActivity(View view) {
        int position = recyclerViewFeed.getChildLayoutPosition(view);
        ActivityFeed activityFeed = activityFeeds.get(position);
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.FEED_OBJ, activityFeed);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
