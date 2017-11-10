package pup.com.gsouapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pup.com.gsouapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mLoginButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        mUsernameView.setError(null);
        mPasswordView.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_required_value));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_required_value));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);

            final Intent intent = new Intent(this, MainActivity.class);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) + getResources().getString(R.string.login),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != "") {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    sharedPreferences = getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("sourceId", obj.getString("sourceId"));
                                    editor.putString("username", obj.getString("username"));
                                    editor.putString("firstName", obj.getString("firstName"));
                                    editor.putString("middleName", obj.getString("middleName"));
                                    editor.putString("lastName", obj.getString("lastName"));
                                    editor.putString("specialization", obj.getString("specialization"));
                                    editor.putString("programType", obj.getString("programType"));
                                    editor.putString("programId", obj.getString("programId"));
                                    editor.putString("programCode", obj.getString("programCode"));
                                    editor.putString("programDesc", obj.getString("programDesc"));
                                    editor.putString("campus", obj.getString("campus"));
                                    editor.putString("campusId", obj.getString("campusId"));
                                    editor.putString("role", obj.getString("role"));
                                    editor.putString("status", obj.getString("status"));
                                    editor.commit();

                                    Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error in Volley", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN", error.toString());
                        }
                    }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", mUsernameView.getText().toString());
                            params.put("password", mPasswordView.getText().toString());
                            return params;
                        }
                    };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}
