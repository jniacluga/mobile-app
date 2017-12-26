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

public class AddSubject extends Fragment
    implements ResponseHandler, DialogCallbackContract {

    private OnFragmentInteractionListener mListener;
    private static AddSubject addSubject;

    EditText subjectsToAdd;
    EditText reason;
    Button btnSubmit;

    private View view;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    List<String> commaSeparatedValues;

    private static final int SERVICE_APPLICATION_INT = 3;

    SubjectChecklist checklist = SubjectChecklist.getInstance();

    Intent intent;

    public AddSubject() {
        // Required empty public constructor
    }

    public static AddSubject getInstance() {
        if (addSubject == null) {
            addSubject = new AddSubject();
        }

        return addSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_subject, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        return view;
    }

    private void bindViews() {
        subjectsToAdd = (EditText) view.findViewById(R.id.subjectsToAdd);
        reason = (EditText) view.findViewById(R.id.as_reason);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        subjectsToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAddSubject()) {
                    callToServer();
                } else {
                    // TODO Toast to tell what should be done
                }
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

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
        params.put("subjects", TextUtils.join(",", commaSeparatedValues));
        params.put("reason", reason.getText().toString());

        AppToServer.sendRequest(getContext(), Urls.ADD_SUBJECT + Urls.SUBMIT, this, params);
    }

    private boolean validateAddSubject() {

        if (reason.getText().toString().equals("") || commaSeparatedValues.size() < 1) {
            return false;
        }

        return true;
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

    @Override
    public void passDataBackToFragment(Map<String, List<String>> map) {
        this.commaSeparatedValues = map.get("subjectIds");
        subjectsToAdd.setText("");
        subjectsToAdd.setText( TextUtils.join(", ", map.get("subjectDescriptions")) );
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
