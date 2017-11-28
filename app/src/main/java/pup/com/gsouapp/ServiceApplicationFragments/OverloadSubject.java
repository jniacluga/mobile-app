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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pup.com.gsouapp.Activities.MainActivity;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Interfaces.DialogCallbackContract;
import pup.com.gsouapp.Interfaces.ResponseHandler;
import pup.com.gsouapp.R;
import pup.com.gsouapp.SubjectChecklist;

public class OverloadSubject extends Fragment
    implements ResponseHandler, DialogCallbackContract {

    private OnFragmentInteractionListener mListener;
    private View view;

    private static OverloadSubject overloadSubject;

    private static final String OTHERS = "Others";

    private static final int SERVICE_APPLICATION_INT = 3;

    EditText subjectsToOverload;
    EditText reason;
    Spinner studentStatus;
    EditText studentStatusOther;

    Button btnSubmit;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    List<String> commaSeparatedValues;

    SubjectChecklist checklist = SubjectChecklist.getInstance();

    Intent intent;

    public OverloadSubject() {

    }

    public static OverloadSubject getInstance() {
        if (overloadSubject == null) {
            overloadSubject = new OverloadSubject();
        }

        return overloadSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_overload_subject, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        return view;
    }

    private void bindViews() {

        subjectsToOverload = (EditText) view.findViewById(R.id.subjectsToOverload);
        reason = (EditText) view.findViewById(R.id.os_reason);
        studentStatus = (Spinner) view.findViewById(R.id.studentStatus);
        studentStatusOther = (EditText) view.findViewById(R.id.studentStatusOther);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        subjectsToOverload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callToServer();
            }
        });
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

        String status = "";

        if (studentStatus.getSelectedItem().toString().equals(OTHERS)) {
            status = studentStatusOther.getText().toString();
        } else {
            status = studentStatus.getSelectedItem().toString();
        }

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
        params.put("subjects", TextUtils.join(",", commaSeparatedValues));
        params.put("studentStatus", status);
        params.put("reason", reason.getText().toString());

        AppToServer.sendRequest(getContext(), Urls.OVERLOAD_SUBJECT + Urls.SUBMIT, this, params);
    }

    @Override
    public void handleResponse(String response) {
        if (!response.equals("\r\n\"\"")) {

            if (response.contains("1")) {
                intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("selectedPage", SERVICE_APPLICATION_INT);
                startActivity(intent);
                getActivity().finish();
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

    @Override
    public void passDataBackToFragment(Map<String, List<String>> map) {
        this.commaSeparatedValues = map.get("subjectIds");
        subjectsToOverload.setText("");
        subjectsToOverload.setText( TextUtils.join(", ", map.get("subjectDescriptions")) );
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
