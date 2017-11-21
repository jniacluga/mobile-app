package pup.com.gsouapp.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

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

import pup.com.gsouapp.Helpers.VolleyClass;
import pup.com.gsouapp.R;
import pup.com.gsouapp.Domain.ServiceApplication;
import pup.com.gsouapp.Adapters.ServiceApplicationRecyclerViewAdapter;

public class ServiceApplicationApproverFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private List<ServiceApplication> serviceApplications;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Spinner statusFilter;
    private Spinner serviceApplicationFilter;
    private Button createServiceApplicationButton;

    private String serviceApplicationType;

    SharedPreferences sharedPreferences;

    RequestQueue queue;

    public ServiceApplicationApproverFragment() {

    }

    public static ServiceApplicationApproverFragment newInstance() {
        ServiceApplicationApproverFragment fragment = new ServiceApplicationApproverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_application, container, false);

        statusFilter = (Spinner) view.findViewById(R.id.status_filter);
        statusFilter.setVisibility(View.GONE);
        serviceApplicationFilter = (Spinner) view.findViewById(R.id.service_application_filter);
        createServiceApplicationButton = (Button) view.findViewById(R.id.create_service_application);
        createServiceApplicationButton.setVisibility(View.GONE);

        serviceApplicationType = "";

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        queue = VolleyClass.getInstance(getContext()).getRequestQueue();

        serviceApplications = new ArrayList<>();

        bindViews();

        queue.add(constructStringRequest(""));

        return view;
    }

    private StringRequest constructStringRequest(final String type) {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                constructUrl(type), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                serviceApplications.clear();

                if (!response.equals("\r\n\"\"")) {

                    try {

                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            ServiceApplication application = new ServiceApplication(
                                    obj.getLong("id"),
                                    obj.getString("id"),
                                    obj.getString("status"),
                                    obj.getString("dateRequested"),
                                    obj.getString("type"),
                                    constructSummary(obj)
                            );

                            serviceApplications.add(application);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new ServiceApplicationRecyclerViewAdapter(serviceApplications);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);

                params.put("facultyId", sharedPreferences.getString("sourceId", ""));
                params.put("status", "For Approval");

                return params;
            }
        };

        return request;
    }

    private String constructSummary(JSONObject obj) throws JSONException {

        String summary = "";

        switch(obj.getString("type")) {
            case "Academic Records":
                summary = obj.getString("numberOfRecordsRequested") + " records including " + obj.getString("requestDocuments").substring(0, 40) + "...";
                break;
            default:
                break;
        }

        return summary;

    }

    private String constructUrl(String type) {

        String url = "";

        switch (type) {
            case "Add Subject":
                url = getResources().getString(R.string.service_application_addSubject);
                break;
            case "Change Subject":
                url = getResources().getString(R.string.service_application_changeSubject);
                break;
            case "Drop Subject":
                url = getResources().getString(R.string.service_application_dropSubject);
                break;
            case "Overload Subject":
                url = getResources().getString(R.string.service_application_overloadSubject);
                break;
            case "Completion":
                url = getResources().getString(R.string.service_application_completion);
                break;
            case "Leave of Absence":
                url = getResources().getString(R.string.service_application_leaveOfAbsence);
                break;
            case "Comprehensive Exam":
                url = getResources().getString(R.string.service_application_comprehensiveExam);
                break;
            case "Petition/Tutorial Class":
                url = getResources().getString(R.string.service_application_petitionTutorial);
                break;
            case "Academic Records":
                url = getResources().getString(R.string.service_application_acadRecords);
                break;
            case "Graduation":
                url = getResources().getString(R.string.service_application_graduation);
                break;
            default:
                url = getResources().getString(R.string.service_application_addSubject);
        }

        return url + "loadAllApplicationsApprover/";
    }

    private void bindViews() {
        serviceApplicationFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                serviceApplicationType = serviceApplicationFilter.getItemAtPosition(position).toString();
                queue.add(constructStringRequest(serviceApplicationType));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
