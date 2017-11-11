package pup.com.gsouapp.Helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import pup.com.gsouapp.Interfaces.ResponseHandler;

public class AppToServer {

    private static RequestQueue mQueue;
    private static String mUrl;
    private static ResponseHandler mHandler;
    private static Map<String, String> mParams;

    public static void sendRequest(Context context, final String url, final ResponseHandler handler, final Map<String, String> params) {

        mQueue = VolleyClass.getInstance(context).getRequestQueue();
        mUrl = url;
        mHandler = handler;
        mParams = params;

        mQueue.add(createRequest());
    }

    private static StringRequest createRequest() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Urls.WEB_SERVER + mUrl,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mHandler.handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.handleErrorResponse(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return mParams;
            }
        };

        return request;
    }
}
