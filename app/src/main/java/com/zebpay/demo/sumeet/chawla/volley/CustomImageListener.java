package com.zebpay.demo.sumeet.chawla.volley;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.zebpay.demo.sumeet.chawla.utility.Utils;

import java.io.IOException;

/**
 * Created by sumeet on 23/12/15.
 */
public class CustomImageListener implements ImageLoader.ImageListener {

    private ImageView imgDisplay;
    private Context context;
    private String friendId;

    public CustomImageListener(ImageView imgDisplay, Context context) {
        this.imgDisplay = imgDisplay;
        this.context = context;
    }

    public CustomImageListener(ImageView imgDisplay, Context context, String friendId) {
        this.imgDisplay = imgDisplay;
        this.context = context;
        this.friendId = friendId;
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        if (response != null && response.getBitmap() != null && imgDisplay != null) {
            if (friendId != null) {
                //Save bitmap in Folder!
                try {
                    Utils.saveToInternalStorage(response.getBitmap(), friendId + ".jpg", context);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Save the path in DB!

                saveImageInDB(friendId, (friendId + ".jpg"), context);
                //imgDisplay.setImageDrawable(new BitmapDrawable(context.getResources(), response.getBitmap()));
            } //else {
            imgDisplay.setImageBitmap(response.getBitmap());
            //}
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        new CustomErrorListener(this.context).onErrorResponse(error);
    }

    public void saveImageInDB(String friendId, String compressThumb, Context context) {
        //VolleyUtils.updateFriendImg(friendId, compressThumb, context);
    }
}
