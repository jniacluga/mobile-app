package pup.com.gsouapp.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pup.com.gsouapp.Activities.LoginActivity;
import pup.com.gsouapp.R;

public class HomeFragment extends Fragment {

    TextView txtVwStudentNumber;
    TextView txtVwStatus;
    TextView txtVwLastName;
    TextView txtVwFirstName;
    TextView txtVwMiddleName;
    TextView txtVwProgram;

    TextView txtVwTimeLeft;
    TextView txtVwCompletedUnits;
    TextView txtVwComprehensiveExam;
    TextView txtVwThesisDissertation;
    TextView txtVwGraduation;

    Button btnLogout;

    SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);

        txtVwFirstName = (TextView) view.findViewById(R.id.homeFirstName);
        txtVwLastName = (TextView) view.findViewById(R.id.homeLastName);
        txtVwMiddleName = (TextView) view.findViewById(R.id.homeMiddleName);
        txtVwProgram = (TextView) view.findViewById(R.id.homeProgram);
        txtVwStatus = (TextView) view.findViewById(R.id.homeStudentStatus);
        txtVwStudentNumber = (TextView) view.findViewById(R.id.homeStudentNumber);

        txtVwTimeLeft = (TextView) view.findViewById(R.id.txtVwTimeLeft);
        txtVwCompletedUnits = (TextView) view.findViewById(R.id.txtVwCompletedUnits);
        txtVwComprehensiveExam = (TextView) view.findViewById(R.id.txtVwComprehensiveExam);
        txtVwThesisDissertation = (TextView) view.findViewById(R.id.txtVwThesisDissertation);
        txtVwGraduation = (TextView) view.findViewById(R.id.txtVwGraduation);

        btnLogout = (Button) view.findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().commit();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        initializeViews();

        return view;
    }

    private void initializeViews() {

        txtVwStudentNumber.setText(sharedPreferences.getString("sourceId", ""));
        txtVwStatus.setText(sharedPreferences.getString("status", ""));
        txtVwFirstName.setText(sharedPreferences.getString("firstName", ""));
        txtVwMiddleName.setText(sharedPreferences.getString("middleName", ""));
        txtVwLastName.setText(sharedPreferences.getString("lastName", ""));
        txtVwProgram.setText(sharedPreferences.getString("programDesc", ""));

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.load_home), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.equals("\r\n\"\"")) {

                            try {

                                JSONObject obj = new JSONArray(response).getJSONObject(1);

                                txtVwTimeLeft.setText("Time left in residency: " + obj.getString("timeLeft"));
                                txtVwCompletedUnits.setText(obj.getString("completedUnits"));
                                txtVwComprehensiveExam.setText(obj.getString("comprehensiveExam"));
                                txtVwThesisDissertation.setText(obj.getString("thesisDissertation"));
                                txtVwGraduation.setText(obj.getString("graduation"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOGIN", error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
