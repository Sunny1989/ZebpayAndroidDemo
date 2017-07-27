package com.zebpay.demo.sumeet.chawla.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumeet on 8/12/15.
 */
public class VolleyGlobal {

    private static VolleyGlobal mInstance;
    private static Context mCtx;
    public static final String TAG = VolleyGlobal.class.getSimpleName();
    private static HashMap<String, String> headers = new HashMap<String, String>();

    private VolleyGlobal(Context context) {
        mCtx = context;
    }


    public static synchronized VolleyGlobal getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyGlobal(context);
        }
        return mInstance;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        //headers.put("Content-Type", "application/json");
        return headers;
    }

    //As we are sending these all params with parameters!
    //Due to previous code flow!

   /* public void setApiKeyInHeader(String key) {
        headers.put(Constants.LBL_X_API_KEY, key);
    }

    public void setAccessToken(String accessToken) {
        headers.put(Constants.ACCESS_TOKEN, accessToken);
    }

    public void setUserId(String userId) {
        headers.put(Constants.USER_ID, userId);
    }

    public void removeAccessTokenFromHeader() {
        headers.remove(Constants.ACCESS_TOKEN);
    }

    public void removeUserIdFromHeader() {
        headers.remove(Constants.USER_ID);
    }

    public void removeApiKeyFromHeader() {
        headers.remove(Constants.LBL_X_API_KEY);
    }*/

}
