package pup.com.gsouapp.ServiceApplicationFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pup.com.gsouapp.Domain.Subject;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Interfaces.DialogCallbackContract;
import pup.com.gsouapp.Interfaces.ResponseHandler;
import pup.com.gsouapp.R;
import pup.com.gsouapp.SubjectChecklist;

public class PetitionTutorialClass extends Fragment
    implements ResponseHandler {

    private OnFragmentInteractionListener mListener;
    private View view;

    Spinner subjectToPetition;
    RadioButton rdoJoin;
    RadioButton rdoCreate;
    ScrollView svStudents;
    ListView lvStudents;
    ScrollView svPetitions;
    ListView lvPetitions;
    Button btnSubmit;

    private String methodName;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    List<Petition> petitions = new ArrayList<>();

    List<String> commaSeparatedValues;

    SubjectChecklist checklist = SubjectChecklist.getInstance();

    Intent intent;

    int petitionId;

    public PetitionTutorialClass() { }

    public static PetitionTutorialClass newInstance(String param1, String param2) {
        PetitionTutorialClass fragment = new PetitionTutorialClass();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_petition_tutorial_class, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        return view;
    }

    private void bindViews() {

        subjectToPetition = (Spinner) view.findViewById(R.id.subjectToPetition);
        rdoJoin = (RadioButton) view.findViewById(R.id.rdoJoin);
        rdoCreate = (RadioButton) view.findViewById(R.id.rdoCreate);
        svStudents = (ScrollView) view.findViewById(R.id.svStudents);
        lvStudents = (ListView) view.findViewById(R.id.lvStudents);
        svPetitions = (ScrollView) view.findViewById(R.id.svPetitions);
        lvPetitions = (ListView) view.findViewById(R.id.lvPetitions);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        subjectToPetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                methodName = Urls.GET_PETITIONS_BY_SUBJECT;
                callToServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callToServer();
            }
        });

        rdoCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideFields();
            }
        });

        rdoJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideFields();
            }
        });

    }

    private void loadPetitions(String response) {

        petitions.clear();

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

                    petitions.add(null);

                    bindPetitionList();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindPetitionList() {

    }

    private void showHideFields() {
        if (rdoJoin.isChecked()) {
            svStudents.setVisibility(View.GONE);
            svPetitions.setVisibility(View.VISIBLE);
        } else {
            svStudents.setVisibility(View.VISIBLE);
            svPetitions.setVisibility(View.GONE);
        }
    }

    public void showDialogFragment() {
        if (!checklist.isAdded()) {
            checklist.setTargetFragment(this, 0);
            checklist.setMethodName(Urls.OVERLOAD_SUBJECT + Urls.GET_SUBJECTS_OFFERED_BUT_NOT_TAKEN_OR_ENROLLED);
            checklist.show(getFragmentManager(), "");
        } else {
            checklist.getDialog().show();
        }
    }

    @Override
    public void callToServer() {
        params = new HashMap<>();

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

        if (methodName.equals(Urls.SUBMIT)) {
            params.put("subject", subjectToPetition.getSelectedItem().toString());
            params.put("students", TextUtils.join(",", commaSeparatedValues));
        } else if (methodName.equals(Urls.JOIN)) {
            params.put("petitionId", String.valueOf(petitionId));
        }

        AppToServer.sendRequest(getContext(), Urls.PETITION_TUTORIAL + methodName, this, params);
    }

    @Override
    public void handleResponse(String response) {
        if (methodName.equals(Urls.GET_PETITIONS_BY_SUBJECT)) {
            loadPetitions(response);
        } else if (methodName.equals(Urls.SUBMIT)) {
            afterSubmission(response);
        }
    }

    private void afterSubmission(String response) {

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
        void onFragmentInteraction(Uri uri);
    }
}
