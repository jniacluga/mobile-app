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
import android.widget.ArrayAdapter;
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

    private static PetitionTutorialClass petitionTutorialClass;

    Spinner subjectToPetition;
    RadioButton rdoJoin;
    RadioButton rdoCreate;
    ScrollView svStudents;
    ListView lvStudents;
    ScrollView svPetitions;
    ListView lvPetitions;
    Button btnSubmit;

    private String methodName;

    private Long subjectToPetitionId;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    List<Petition> petitions = new ArrayList<>();
    List<Student> students = new ArrayList<>();
    List<Subject> subjects = new ArrayList<>();

    List<String> commaSeparatedValues;

    SubjectChecklist checklist = SubjectChecklist.getInstance();

    Intent intent;

    int petitionId;

    public PetitionTutorialClass() { }

    public static PetitionTutorialClass getInstance() {
        if (petitionTutorialClass == null) {
            petitionTutorialClass = new PetitionTutorialClass();
        }
        return petitionTutorialClass;
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
        loadSubject();
        return view;
    }

    private void loadSubject() {
        methodName = Urls.GET_SUBJECTS_NOT_OFFERED_AND_NOT_TAKEN_OR_ENROLLED;
        callToServer();
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
                subjectToPetitionId = ((Subject)subjectToPetition.getSelectedItem()).getSubjectId();

                if (rdoJoin.isChecked()) {
                    methodName = Urls.GET_PETITIONS_BY_SUBJECT;
                } else {
                    methodName = Urls.GET_STUDENTS_WHO_REQUIRE_THE_SUBJECT;
                }

                callToServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoJoin.isChecked()) {
                    methodName = Urls.JOIN;
                } else {
                    methodName = Urls.SUBMIT;
                }

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

    private void loadStudents(String response) {

    }

    private void loadSubjects(String response) {
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

                ArrayAdapter<Subject> adapter = new ArrayAdapter<Subject>(getContext(), R.layout.support_simple_spinner_dropdown_item, subjects);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                subjectToPetition.setAdapter(adapter);

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

    @Override
    public void callToServer() {
        params = new HashMap<>();

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

        if (methodName.equals(Urls.SUBMIT)) {
            params.put("subject", subjectToPetition.getSelectedItem().toString());
            params.put("students", "");
        } else if (methodName.equals(Urls.JOIN)) {
            params.put("petitionId", String.valueOf(petitionId));
        } else {
            params.put("subjectId", subjectToPetitionId.toString());
        }

        AppToServer.sendRequest(getContext(), Urls.PETITION_TUTORIAL + methodName, this, params);
    }

    @Override
    public void handleResponse(String response) {
        if (methodName.equals(Urls.GET_PETITIONS_BY_SUBJECT)) {
            loadPetitions(response);
        } else if (methodName.equals(Urls.GET_STUDENTS_WHO_REQUIRE_THE_SUBJECT)) {
            loadStudents(response);
        } else if (methodName.equals(Urls.GET_SUBJECTS_NOT_OFFERED_AND_NOT_TAKEN_OR_ENROLLED)) {
            loadSubjects(response);
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
