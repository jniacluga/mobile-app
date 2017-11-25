package pup.com.gsouapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pup.com.gsouapp.Adapters.SubjectsAdapter;
import pup.com.gsouapp.Domain.Schedule;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Interfaces.DialogCallbackContract;
import pup.com.gsouapp.Interfaces.ResponseHandler;

public class SubjectChecklist extends DialogFragment
    implements ResponseHandler {

    private OnFragmentInteractionListener mListener;
    private View view;

    private String methodName;

    private List<Schedule> schedules = new ArrayList<>();

    private static SubjectChecklist subjectChecklist;

    private ListView lvSubjects;
    private Button btnAdd;

    List<Boolean> checkBoxState;

    SubjectsAdapter adapter;

    Context mContext;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    Intent intent;

    public SubjectChecklist() {
        mContext = getActivity();
    }

    public static SubjectChecklist getInstance() {
        if (subjectChecklist == null) {
            subjectChecklist = new SubjectChecklist();
        }

        return subjectChecklist;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subject_checklist, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindView();
        callToServer();
        return view;
    }

    private void bindView() {

        lvSubjects = (ListView) view.findViewById(R.id.lvSubjects);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkBoxState = adapter.getCheckboxState();

                List<String> subjectIds = new ArrayList<>();
                List<String> subjectDescriptions = new ArrayList<>();

                int i = 0;
                for (Boolean state : checkBoxState) {
                    if (state) {
                        subjectIds.add(schedules.get(i).getScheduleId().toString());
                        subjectDescriptions.add(schedules.get(i).getSubjectCode());
                    }
                    i++;
                }

                HashMap<String, List<String>> map = new HashMap<>();
                map.put("subjectIds", subjectIds);
                map.put("subjectDescriptions", subjectDescriptions);
                ((DialogCallbackContract) getTargetFragment()).passDataBackToFragment(map);
                getDialog().hide();
            }
        });
    }

    @Override
    public void callToServer() {
        params = new HashMap<>();

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

        AppToServer.sendRequest(getContext(), methodName, this, params);
    }

    @Override
    public void handleResponse(String response) {
        if (!response.equals("\r\n\"\"")) {
            loadChecklist(response);
        }
    }

    private void loadChecklist(String response) {

        try {

            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                Schedule schedule = new Schedule(
                    obj.getString("sy"),
                    obj.getString("sem"),
                    obj.getString("day"),
                    obj.getString("startTime"),
                    obj.getString("endTime"),
                    obj.getString("subjectCode"),
                    obj.getString("subjectDesc"),
                    obj.getString("faculty"),
                    obj.getLong("scheduleId"),
                    obj.getLong("subjectId"),
                    obj.getInt("units")
                );

                schedules.add(schedule);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkBoxState = new ArrayList<>(Collections.nCopies(schedules.size(), false));
        lvSubjects.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter = new SubjectsAdapter(getContext(), schedules, checkBoxState);
        lvSubjects.setAdapter(adapter);
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
        void onFragmentInteraction(Uri uri);
    }
}
