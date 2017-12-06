package pup.com.gsouapp.ServiceApplicationFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pup.com.gsouapp.Activities.MainActivity;
import pup.com.gsouapp.Adapters.SubjectsAdapterNoCheckbox;
import pup.com.gsouapp.Domain.Subject;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Interfaces.ResponseHandler;
import pup.com.gsouapp.R;

public class ComprehensiveExam extends Fragment
    implements ResponseHandler {

    private OnFragmentInteractionListener mListener;
    private View view;

    private static ComprehensiveExam comprehensiveExam;

    private static final int SERVICE_APPLICATION_INT = 3;

    private String methodName;

    ListView lvCurrentlyEnrolledSubjects;
    ListView lvAlreadyTakenSubjects;
    Button btnSubmit;

    List<Subject> subjects = new ArrayList<>();

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    BaseAdapter adapter;

    Intent intent;

    public ComprehensiveExam() {
        // Required empty public constructor
    }

    public static ComprehensiveExam getInstance() {
        if (comprehensiveExam == null) {
            comprehensiveExam = new ComprehensiveExam();
        }

        return comprehensiveExam;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comprehensive_exam, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        loadCurrentlyEnrolledSubjects();
        return view;
    }

    private void bindViews() {
        lvCurrentlyEnrolledSubjects = (ListView) view.findViewById(R.id.lvCurrentlyEnrolledSubjects);
        lvAlreadyTakenSubjects = (ListView) view.findViewById(R.id.lvAlreadyTakenSubjects);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodName = Urls.SUBMIT;
                callToServer();
            }
        });
    }

    private void loadCurrentlyEnrolledSubjects() {
        methodName = Urls.GET_CURRENTLY_ENROLLED_SUBJECTS;
        callToServer();
    }

    private void loadAlreadyTakenSubjects() {
        methodName = Urls.GET_ALREADY_COMPLETED_SUBJECTS;
        callToServer();
    }

    private void populateCurrentlyEnrolledSubjects(String response) {
        subjects.clear();

        if (!response.equals("\r\n\"\"")) {

            try {

                JSONArray arr = new JSONArray(response);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    Subject subject = new Subject(
                            obj.getLong("subjectId"),
                            obj.getString("subjectDisplay"),
                            obj.getString("code"),
                            obj.getString("description"),
                            obj.getInt("units"),
                            obj.getString("sy"),
                            obj.getString("semester")
                    );

                    subjects.add(subject);
                }

                adapter = new SubjectsAdapterNoCheckbox(getContext(), subjects);
                lvCurrentlyEnrolledSubjects.setAdapter(adapter);

                loadAlreadyTakenSubjects();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateAlreadyTakenSubjects(String response) {
        subjects.clear();

        if (!response.equals("\r\n\"\"")) {

            try {

                JSONArray arr = new JSONArray(response);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    Subject subject = new Subject(
                            obj.getLong("subjectId"),
                            obj.getString("subjectDisplay"),
                            obj.getString("code"),
                            obj.getString("description"),
                            obj.getInt("units"),
                            obj.getString("sy"),
                            obj.getString("semester")
                    );

                    subjects.add(subject);
                }

                adapter = new SubjectsAdapterNoCheckbox(getContext(), subjects);
                lvAlreadyTakenSubjects.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void afterSubmission(String response) {
        if (!response.equals("\r\n\"\"")) {

            if (response.contains("1")) {
                intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("selectedPage", SERVICE_APPLICATION_INT);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "An error has been encountered, try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void callToServer() {
        params = new HashMap<>();

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

        AppToServer.sendRequest(getContext(), Urls.COMPREHENSIVE_EXAM + methodName, this, params);
    }
    @Override
    public void handleResponse(String response) {
        if (methodName.equals(Urls.SUBMIT)) {
            afterSubmission(response);
        } else if (methodName.equals(Urls.GET_CURRENTLY_ENROLLED_SUBJECTS)) {
            populateCurrentlyEnrolledSubjects(response);
        } else if (methodName.equals(Urls.GET_ALREADY_COMPLETED_SUBJECTS)) {
            populateAlreadyTakenSubjects(response);
        }
    }

    @Override
    public void handleErrorResponse(VolleyError error) {

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
