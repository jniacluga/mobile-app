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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import pup.com.gsouapp.Domain.Subject;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Interfaces.ResponseHandler;
import pup.com.gsouapp.R;

public class Completion extends Fragment
    implements ResponseHandler{

    private OnFragmentInteractionListener mListener;
    private View view;

    private static Completion completion;

    private static final int SERVICE_APPLICATION_INT = 3;

    private Spinner applicationForSpnr;
    private Spinner completionSubjectSpnr;
    private Spinner reportedAsSpnr;
    private EditText reportedAsOther;
    private EditText reason;
    private Spinner beCreditedAsOneSpnr;
    private EditText beCreditedDetails;
    private EditText nameTo;

    private Button btnSubmit;

    private String methodName;

    Map<String, String> params;
    SharedPreferences sharedPreferences;
    List<Subject> subjects = new ArrayList<>();

    Intent intent;

    public Completion() {

    }

    public static Completion getInstance() {
        if (completion == null) {
            completion = new Completion();
        }

        return completion;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_completion, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        return view;
    }

    private void bindViews() {

        applicationForSpnr = (Spinner) view.findViewById(R.id.applicationFor);
        completionSubjectSpnr = (Spinner) view.findViewById(R.id.completionSubject);
        reportedAsSpnr = (Spinner) view.findViewById(R.id.reportedAs);
        reportedAsOther = (EditText) view.findViewById(R.id.reportedAsOther);
        reason = (EditText) view.findViewById(R.id.cp_reason);
        beCreditedAsOneSpnr = (Spinner) view.findViewById(R.id.beCreditedAsOne);
        beCreditedDetails = (EditText) view.findViewById(R.id.beCreditedDetails);
        nameTo = (EditText) view.findViewById(R.id.nameTo);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodName = Urls.SUBMIT;
                callToServer();
            }
        });

        methodName = Urls.GET_CURRENTLY_ENROLLED_AND_TAKEN_SUBJECTS;
        callToServer();
    }

    @Override
    public void callToServer() {

        params = new HashMap<>();

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

        if (methodName.equals(Urls.SUBMIT)) {
            params.put("applicationFor", applicationForSpnr.getSelectedItem().toString());
            params.put("reportedAs", !reportedAsOther.getText().toString().equals("") ? reportedAsOther.getText().toString() : reportedAsSpnr.getSelectedItem().toString());
            params.put("reason", reason.getText().toString());
            params.put("subject", ((Subject)completionSubjectSpnr.getSelectedItem()).getSubjectId().toString());
            params.put("beCreditedAs", beCreditedAsOneSpnr.getSelectedItem().toString());
            params.put("beCreditedAsDetail", beCreditedDetails.getText().toString());
            params.put("nameTo", nameTo.getText().toString());
        }

        AppToServer.sendRequest(getContext(), Urls.COMPLETION + methodName, this, params);
    }

    @Override
    public void handleResponse(String response) {
        if (methodName.equals(Urls.GET_CURRENTLY_ENROLLED_AND_TAKEN_SUBJECTS)) {
            bindSubjectSpinner(response);
        } else if (methodName.equals(Urls.SUBMIT)) {
            afterSubmission(response);
        }
    }

    private void bindSubjectSpinner(String response) {

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

                    bindSubject();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindSubject() {

        ArrayAdapter<Subject> adapter = new ArrayAdapter<Subject>(getContext(), R.layout.support_simple_spinner_dropdown_item, subjects);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        completionSubjectSpnr.setAdapter(adapter);
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
    public void handleErrorResponse(VolleyError error) {

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
