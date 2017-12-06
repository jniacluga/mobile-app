package pup.com.gsouapp.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import pup.com.gsouapp.Helpers.Urls;
import pup.com.gsouapp.Helpers.VolleyClass;
import pup.com.gsouapp.R;

public class ViewServiceApplicationActivity extends AppCompatActivity {

    TextView applicationNumber;
    TextView status;
    TextView approvalLevel;
    TextView numberOfApprovers;
    TextView dateRequested;
    TextView schoolYearSemester;

    Button cancelBtn;
    Button approveBtn;
    Button declineBtn;

    Long appId;
    String appType;
    String appStatus;

    SharedPreferences sharedPreferences;
    AlertDialog alert;

    RequestQueue queue;

    private static final int SERVICE_APPLICATION_INT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            appId = extras.getLong("appId");
            appType = extras.getString("appType");
            appStatus = extras.getString("appStatus");

            queue = VolleyClass.getInstance(this).getRequestQueue();
            queue.add(constructStringRequest("", ""));

            switch(appType) {
                case "Add Subject":
                    setUpAddSubject();
                    break;
                case "Change Subject":
                    setUpChangeSubject();
                    break;
                case "Drop Subject":
                    setUpDropSubject();
                    break;
                case "Overload Subject":
                    setUpOverloadSubject();
                    break;
                case "Completion":
                    setUpCompletion();
                    break;
                case "Leave of Absence":
                    setUpLeaveOfAbsence();
                    break;
                case "Comprehensive Exam":
                    setUpComprehensiveExam();
                    break;
                case "Petition/Tutorial Class":
                    setUpPetitionTutorialClasses();
                    break;
                case "Academic Records":
                    setUpAcademicRecords();
                    break;
                case "Graduation":
                    setUpGraduation();
                    break;
                default:
                    break;
            }
        }
    }

    private void setUpCommon(final String applicationNumberStr, final String statusStr, final String approvalLevelStr, final String dateRequestedStr) {
        applicationNumber = (TextView) findViewById(R.id.view_application_number);
        status = (TextView) findViewById(R.id.view_status);
        approvalLevel = (TextView) findViewById(R.id.view_approval_level);
        dateRequested = (TextView) findViewById(R.id.view_date_requested);
//        schoolYearSemester = (TextView) findViewById(R.id.view_school_year_semester);

        applicationNumber.setText(applicationNumberStr);
        status.setText(statusStr);
        approvalLevel.setText(approvalLevelStr);
        dateRequested.setText(dateRequestedStr);
//        schoolYearSemester.setText("");

        setUpButtons();
    }

    private void setUpButtons() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        alert = null;

                    }
                });

        cancelBtn = (Button) findViewById(R.id.cancel_button);
        approveBtn = (Button) findViewById(R.id.approve_button);
        declineBtn = (Button) findViewById(R.id.decline_button);

        String role = sharedPreferences.getString("role", "");

        if (!role.equals("") && role.equals("STUDENT") && (!appStatus.equals("Cancelled") && !appStatus.equals("Approved")) && !appStatus.equals("Declined")) {
            cancelBtn.setVisibility(View.VISIBLE);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setMessage("Are you sure you wish to cancel this application?");

                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    queue.add(constructStringRequest(appType, "Cancelled"));
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("selectedPage", SERVICE_APPLICATION_INT);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    alert = builder.create();
                    alert.show();
                }
            });

        } else if (!role.equals("") && !role.equals("STUDENT")) {
            approveBtn.setVisibility(View.VISIBLE);
            declineBtn.setVisibility(View.VISIBLE);

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setMessage("Are you sure you wish to approve this application?");

                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    queue.add(constructStringRequest(appType, "Approved"));
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("selectedPage", SERVICE_APPLICATION_INT);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    alert = builder.create();
                    alert.show();
                }
            });

            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setMessage("Are you sure you wish to decline this application?");

                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    queue.add(constructStringRequest(appType, "Declined"));
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("selectedPage", SERVICE_APPLICATION_INT);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    alert = builder.create();
                    alert.show();
                }
            });
        }
    }

    private StringRequest constructStringRequest(final String type, final String action) {

        final Long appIdFinal = appId;
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                constructUrl(type, action), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    if (response.contains("Success")) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT);
                    }
                }
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

                params.put("appId", appIdFinal.toString());
                params.put("sourceId", sharedPreferences.getString("sourceId", ""));
                params.put("status", action);

                return params;
            }
        };

        return request;
    }

    private String constructUrl(String type, String action) {

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

        if (action.equals("Cancelled")) {
            return url + "cancelApplication/";
        } else {
            return url + "reviewApplication/";
        }
    }

    private void setUpAddSubject() {

        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.ADD_SUBJECT + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_add_subject);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView numberOfSubjects = (TextView) findViewById(R.id.view_as_number_of_subjects);
                            TextView listOfSubjects = (TextView) findViewById(R.id.view_as_list_of_subjects);
                            TextView reason = (TextView) findViewById(R.id.view_as_reason);

                            numberOfSubjects.setText(obj.getString("numberOfSubjects"));
                            listOfSubjects.setText(obj.getString("subjects"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void setUpChangeSubject() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.CHANGE_SUBJECT + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_change_subject);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView numberOfToSubjects = (TextView) findViewById(R.id.view_cs_number_of_subjects_to_change);
                            TextView numberOfFromSubjects = (TextView) findViewById(R.id.view_cs_number_of_subjects_from_change);
                            TextView listOfFromSubjects = (TextView) findViewById(R.id.view_cs_list_of_original_subjects);
                            TextView listOfToSubjects = (TextView) findViewById(R.id.view_cs_list_of_new_subjects);
                            TextView reason = (TextView) findViewById(R.id.view_cs_reason);

                            numberOfToSubjects.setText(obj.getString("subjectCountChange"));
                            numberOfFromSubjects.setText(obj.getString("subjectCount"));
                            listOfFromSubjects.setText(obj.getString("subjects"));
                            listOfToSubjects.setText(obj.getString("subjectsChange"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void setUpDropSubject() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.DROP_SUBJECT + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_drop_subject);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView numberOfSubjects = (TextView) findViewById(R.id.view_ds_number_of_subjects);
                            TextView listOfSubjects = (TextView) findViewById(R.id.view_ds_list_of_subjects);
                            TextView reason = (TextView) findViewById(R.id.view_ds_reason);

                            numberOfSubjects.setText(obj.getString("numberOfSubjects"));
                            listOfSubjects.setText(obj.getString("subjects"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);


    }

    private void setUpOverloadSubject() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.OVERLOAD_SUBJECT + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_overload_subject);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView numberOfSubjects = (TextView) findViewById(R.id.view_os_number_of_subjects);
                            TextView listOfSubjects = (TextView) findViewById(R.id.view_os_list_of_subjects);
                            TextView studentStatus = (TextView) findViewById(R.id.view_os_student_status);
                            TextView reason = (TextView) findViewById(R.id.view_os_reason);

                            numberOfSubjects.setText(obj.getString("subjectCount"));
                            listOfSubjects.setText(obj.getString("subjects"));
                            studentStatus.setText(obj.getString("studentStatus"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);

//        setUpCommon();
    }

    private void setUpLeaveOfAbsence() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.LEAVE_OF_ABSENCE + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_leave_of_absence);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView dateOfEffectivity = (TextView) findViewById(R.id.view_loa_date_of_effectivity);
                            TextView reason = (TextView) findViewById(R.id.view_loa_reason);

                            dateOfEffectivity.setText(obj.getString("dateOfEffectivity"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void setUpCompletion() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.COMPLETION + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_completion);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView type = (TextView) findViewById(R.id.view_completion_type);
                            TextView subject = (TextView) findViewById(R.id.view_completion_subject);
                            TextView issue = (TextView) findViewById(R.id.view_completion_issue);
                            TextView creditedAs = (TextView) findViewById(R.id.view_completion_credited_as);
                            TextView details = (TextView) findViewById(R.id.view_completion_details);
                            TextView reason = (TextView) findViewById(R.id.view_completion_reason);

                            type.setText(obj.getString("completionType"));
                            subject.setText(obj.getString("subject"));
                            issue.setText(obj.getString("issue"));
                            creditedAs.setText(obj.getString("creditedAs"));
                            details.setText(obj.getString("details"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void setUpPetitionTutorialClasses() {
        setContentView(R.layout.activity_view_petition_tutorial_class);
//        setUpCommon();

        TextView involvedStudentsCount = (TextView) findViewById(R.id.view_ptc_involved_students_count);
        TextView involvedStudents = (TextView) findViewById(R.id.view_ptc_involved_students);
        TextView approvedInviteesCount = (TextView) findViewById(R.id.view_ptc_approved_invitees_count);
        TextView approvedInvitees = (TextView) findViewById(R.id.view_ptc_approved_invitees);
        TextView subject = (TextView) findViewById(R.id.view_ptc_subject);
        TextView campus = (TextView) findViewById(R.id.view_ptc_campus);
        TextView reason = (TextView) findViewById(R.id.view_ptc_reason);

        involvedStudentsCount.setText("");
        involvedStudents.setText("");
        approvedInviteesCount.setText("");
        approvedInvitees.setText("");
        campus.setText("");
        subject.setText("");
        reason.setText("");
    }

    private void setUpAcademicRecords() {
        setContentView(R.layout.activity_view_academic_records);

        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.ACADEMIC_RECORDS + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView requestedDocumentsCount = (TextView) findViewById(R.id.view_ar_record_number_of_records);
                            TextView requestedDocuments = (TextView) findViewById(R.id.view_ar_requested_documents);
                            TextView reason = (TextView) findViewById(R.id.view_ar_reason);

                            requestedDocumentsCount.setText(obj.getString("numberOfRecordsRequested"));
                            requestedDocuments.setText(obj.getString("requestDocuments"));
                            reason.setText(obj.getString("reason"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void setUpComprehensiveExam() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.COMPREHENSIVE_EXAM + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_comprehensive_exam);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView completedSubjects = (TextView) findViewById(R.id.view_compre_completed_subjects);
                            TextView currentlyEnrolledSubjects = (TextView) findViewById(R.id.view_compre_currently_enrolled_subjects);
                            TextView totalNumberOfUnits = (TextView) findViewById(R.id.view_compre_total_number_of_units);

                            completedSubjects.setText(obj.getString("completedSubjects"));
                            currentlyEnrolledSubjects.setText(obj.getString("currentlyEnrolledSubjects"));
                            totalNumberOfUnits.setText(obj.getString("totalNumberOfUnitsToTake"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void setUpGraduation() {
        final Long appIdFinal = appId;

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                Urls.GRADUATION + "loadSingleApplication/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {
                        JSONArray arr = new JSONArray(response);

                        setContentView(R.layout.activity_view_graduation);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            TextView alreadyTakenSubjects = (TextView) findViewById(R.id.view_graduation_already_taken_subjects);
                            TextView comprehensiveExamDate = (TextView) findViewById(R.id.view_graduation_compre_exam);
                            TextView currentlyEnrolledSubjects = (TextView) findViewById(R.id.view_graduation_currently_enrolled_subjects);
                            TextView oralDefenseDate = (TextView) findViewById(R.id.view_graduation_oral_defense_date);

                            alreadyTakenSubjects.setText(obj.getString("completedSubjects"));
                            comprehensiveExamDate.setText(obj.getString("dateCompreExam"));
                            currentlyEnrolledSubjects.setText(obj.getString("currentlyEnrolledSubjects"));
                            oralDefenseDate.setText(obj.getString("dateOfOralDefense"));

                            setUpCommon(obj.getString("id"), obj.getString("status"), obj.getString("approvalLevel") + "/" + obj.getString("numberOfApprovers"), obj.getString("dateRequested"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("appId", appIdFinal.toString());
                return params;
            }
        };

        queue.add(request);
    }
}
