package pup.com.gsouapp.Helpers;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyClass {

    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static VolleyClass mInstance;

    private VolleyClass(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyClass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /*public ImageLoader getImageLoader() {
        return mImageLoader;
    }*/
}
