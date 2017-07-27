package com.zebpay.demo.sumeet.chawla.custom.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zebpay.demo.sumeet.chawla.adapters.FeedAdapter;
import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeed;
import com.zebpay.demo.sumeet.chawla.volley.VolleyUtils;

/**
 * Created by sumeet on 6/6/16.
 */
public class BindingUtils {

    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
    }

    @BindingAdapter("bind:imgUrl")
    public static void setImage(ImageView image, String url) {
        /*if (url.contains("cropped.jpg")) {
            image.setImageDrawable(null);
            Uri uri = Uri.parse(url);
            image.setImageURI(uri);
        } else {*/
        VolleyUtils.downloadAndSetImage(image.getContext(), image, url);
        //}
    }

    @BindingAdapter("bind:items")
    public static void bindList(RecyclerView view, ObservableArrayList<ActivityFeed> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(new FeedAdapter(view, list, view.getContext()));
    }


}

