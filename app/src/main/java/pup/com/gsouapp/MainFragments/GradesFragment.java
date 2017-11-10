package pup.com.gsouapp.MainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pup.com.gsouapp.Domain.Grade;
import pup.com.gsouapp.Domain.GradePerSySem;
import pup.com.gsouapp.Adapters.GradesAdapter;
import pup.com.gsouapp.Helpers.VolleyClass;
import pup.com.gsouapp.R;

public class GradesFragment extends Fragment {

    private List<GradePerSySem> gradePerSySemList = new ArrayList<>();
    private List<Grade> gradeList = new ArrayList<>();

    private ListView listView;

    RequestQueue queue;

    private OnFragmentInteractionListener mListener;

    public GradesFragment() {

    }

    public static GradesFragment getInstance() {
        GradesFragment fragment = new GradesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grades, container, false);

        queue = VolleyClass.getInstance(getContext()).getRequestQueue();
        populateList(view);

        return view;
    }

    private void populateList(final View view) {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.get_grades), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {

                        gradePerSySemList.clear();
                        gradeList.clear();

                        JSONArray arr = new JSONArray(response);
                        Grade grade;
                        GradePerSySem gradePerSySem = new GradePerSySem();
                        String sySem = "";

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            /*gradePerSySem.setSySem(obj.getString("sySem"));*/
                            gradePerSySem.setSy(obj.getString("sy"));
                            gradePerSySem.setSem(obj.getString("sem"));

                            grade = new Grade(obj.getString("subjectCode"),
                                    obj.getString("description"),
                                    obj.getString("faculty"),
                                    obj.getString("units"),
                                    obj.getString("sectionCode"),
                                    obj.getString("finalGrade"),
                                    obj.getString("status"));
                            gradeList.add(grade);

                            if (!sySem.equals("") && (!obj.getString("sySem").equals(sySem) || i == arr.length() - 1)) {
                                gradePerSySem.setGradeList(gradeList);

                                gradePerSySemList.add(gradePerSySem);

                                grade = null;
                                gradePerSySem = new GradePerSySem();
                            }

                            sySem = obj.getString("sySem");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listView = (ListView) view.findViewById(R.id.lvCustomList);
                listView.setAdapter(new GradesAdapter(getContext(), gradePerSySemList));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
                params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
                return params;
            }
        };

        queue.add(request);

    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
