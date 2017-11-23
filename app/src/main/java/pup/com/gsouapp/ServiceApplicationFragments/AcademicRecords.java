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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import pup.com.gsouapp.Activities.MainActivity;
import pup.com.gsouapp.Helpers.AppToServer;
import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Interfaces.ResponseHandler;
import pup.com.gsouapp.R;

public class AcademicRecords extends Fragment
    implements ResponseHandler{

    private OnFragmentInteractionListener mListener;
    private View view;

    private static AcademicRecords academicRecords;

    private static final String COPY_FOR = "Copy for...";
    private static final String OTHERS = "Others";
    private static final String OFFICIAL_TRANSCRIPT_OF_RECORDS = "Official Transcript of Records";
    private static final String CERTIFICATE_OF_GRADE = "Certificate of Grades";
    private static final String CERTIFICATE_OF_TRANSFER_CREDENTIALS = "Certificate of Transfer Credentials/Honorable Dismissal";
    private static final String CERTIFICATION = "Certification";
    private static final String CERTIFIED_TRUE_COPY = "Certified True Copy";

    private static final int SERVICE_APPLICATION_INT = 3;

    private CheckBox officialTranscriptOfRecordsChkBx;
    private Spinner officialTranscriptOfRecordsSpnr;
    private EditText officialTranscriptOfRecordsOther;

    private CheckBox certificateOfGradesChkBx;

    private CheckBox certificateOfTransferCredentialsChkBx;

    private CheckBox certificationChkBx;
    private Spinner certificationSpnr;

    private CheckBox certifiedTrueCopyChkBx;
    private Spinner certifiedTrueCopySpnr;
    private EditText certifiedTrueCopyOther;

    private Spinner purposeOfRequestSpnr;
    private EditText purposeOfRequestOther;

    private Button btnSubmit;

    Map<String, String> params;
    SharedPreferences sharedPreferences;

    Intent intent;

    public AcademicRecords() {

    }

    public static AcademicRecords getInstance() {
        if (academicRecords == null) {
            academicRecords = new AcademicRecords();
        }

        return academicRecords;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_academic_records, container, false);
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        bindViews();
        return view;
    }

    private void bindViews() {

//        officialTranscriptOfRecordsChkBx = (CheckBox) view.findViewById(R.id.officialTranscriptOfRecordsChkBx);
//        officialTranscriptOfRecordsSpnr = (Spinner) view.findViewById(R.id.officialTranscriptOfRecordsSpnr);
//        officialTranscriptOfRecordsOther = (EditText) view.findViewById(R.id.officialTranscriptOfRecordsOther);
//
//        certificateOfGradesChkBx = (CheckBox) view.findViewById(R.id.certificateOfGradesChkBx);
//
//        certificateOfTransferCredentialsChkBx = (CheckBox) view.findViewById(R.id.certificateOfTransferCredentialsChkBx);
//
//        certificationChkBx = (CheckBox) view.findViewById(R.id.certificationChkBx);
//
//        certificationSpnr = (Spinner) view.findViewById(R.id.certificationSpnr);
//
//        certifiedTrueCopyChkBx = (CheckBox) view.findViewById(R.id.certifiedTrueCopyChkBx);
//        certifiedTrueCopySpnr = (Spinner) view.findViewById(R.id.certifiedTrueCopySpnr);
//        certifiedTrueCopyOther = (EditText) view.findViewById(R.id.certifiedTrueCopyOther);
//
//        purposeOfRequestSpnr = (Spinner) view.findViewById(R.id.purposeOfRequestSpnr);
//        purposeOfRequestOther = (EditText) view.findViewById(R.id.purposeOfRequestOther);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callToServer();
            }
        });
    }

    private void setHiddenFields() {
        // TODO For Sprint: AESTHETICS
    }

    @Override
    public void callToServer() {

        params = new HashMap<>();

        String requestedDocuments = getRequestedDocuments();
        String purpose = "";

        if (purposeOfRequestSpnr.getSelectedItem().toString().equals(OTHERS)) {
            purpose = purposeOfRequestOther.getText().toString();
        } else {
            purpose = purposeOfRequestSpnr.getSelectedItem().toString();
        }

        params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
        params.put("requestedDocuments", requestedDocuments);
        params.put("purpose", purpose);

        AppToServer.sendRequest(getContext(), Urls.ACADEMIC_RECORDS + Urls.SUBMIT, this, params);
    }

    private String getRequestedDocuments() {

        StringBuilder stringBuilder = new StringBuilder();

        if (officialTranscriptOfRecordsChkBx.isChecked()) {

            stringBuilder.append(OFFICIAL_TRANSCRIPT_OF_RECORDS);

            if (officialTranscriptOfRecordsSpnr.getSelectedItem().toString().equals(COPY_FOR)) {
                stringBuilder.append(" (" + officialTranscriptOfRecordsOther.getText().toString() + ");");
            } else {
                stringBuilder.append(" (" + officialTranscriptOfRecordsSpnr.getSelectedItem().toString() + "); ");
            }
        }

        if (certificateOfGradesChkBx.isChecked()) {

            stringBuilder.append(CERTIFICATE_OF_GRADE + "; ");
        }

        if (certificateOfTransferCredentialsChkBx.isChecked()) {

            stringBuilder.append(CERTIFICATE_OF_TRANSFER_CREDENTIALS + "; ");
        }

        if (certificationChkBx.isChecked()) {

            stringBuilder.append(CERTIFICATION);
            stringBuilder.append(" of " + certificationSpnr.getSelectedItem().toString() + "; ");
        }

        if (certifiedTrueCopyChkBx.isChecked()) {

            stringBuilder.append(CERTIFIED_TRUE_COPY);

            if (certifiedTrueCopySpnr.getSelectedItem().toString().equals(OTHERS)) {
                stringBuilder.append(" (" + certifiedTrueCopyOther.getText().toString() + ");");
            } else {
                stringBuilder.append(" (" + certifiedTrueCopySpnr.getSelectedItem().toString() + "); ");
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public void handleResponse(String response) {

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
        void onFragmentInteraction(Uri uri);
    }
}
