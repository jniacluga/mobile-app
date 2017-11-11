package pup.com.gsouapp.Interfaces;

import com.android.volley.VolleyError;

public interface ResponseHandler {

    void callToServer();
    void handleResponse(String response);
    void handleErrorResponse(VolleyError error);
}
