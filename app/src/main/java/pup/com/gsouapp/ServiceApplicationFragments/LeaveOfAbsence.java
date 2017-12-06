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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import pup.com.gsouapp.Activities.MainActivity;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Interfaces.ResponseHandler;
import pup.com.gsouapp.R;

public class LeaveOfAbsence extends Fragment
        implements ResponseHandler{

    private OnFragmentInteractionListener mListener;
    private View view;

    private static LeaveOfAbsence leaveOfAbsence;

    private static final int SERVICE_APPLICATION_INT = 3;

    private EditText dateEffective;
    private EditText reason;

    private Button btnSubmit;

    private String methodName;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    Intent intent;

    public LeaveOfAbsence() {

    }

    public static LeaveOfAbsence getInstance() {
        if (leaveOfAbsence == null) {
            leaveOfAbsence = new LeaveOfAbsence();
        }

        return leaveOfAbsence;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_leave_of_absence, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        return view;
    }

    private void bindViews() {

        dateEffective = (EditText) view.findViewById(R.id.dateEffective);
        reason = (EditText) view.findViewById(R.id.loa_reason);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodName = Urls.SUBMIT;
                callToServer();
            }
        });
    }

    @Override
    public void callToServer() {

        params = new HashMap<>();

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

        if (methodName.equals(Urls.SUBMIT)) {
            params.put("date", dateEffective.getText().toString());
            params.put("reason", reason.getText().toString());
        }

        AppToServer.sendRequest(getContext(), Urls.LEAVE_OF_ABSENCE + methodName, this, params);
    }

    @Override
    public void handleResponse(String response) {
        if (methodName.equals(Urls.SUBMIT)) {
            afterSubmission(response);
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
