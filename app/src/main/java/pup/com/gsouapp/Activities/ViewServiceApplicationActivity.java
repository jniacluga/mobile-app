package pup.com.gsouapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    SharedPreferences sharedPreferences;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            appId = extras.getLong("appId");
            appType = extras.getString("appType");

            queue = VolleyClass.getInstance(this).getRequestQueue();
            queue.add(constructStringRequest("", ""));

            setUpAddSubject();
            setUpChangeSubject();
            setUpDropSubject();
            setUpOverloadSubject();
            setUpLeaveOfAbsence();
            setUpCompletion();
            setUpPetitionTutorialClasses();
            setUpAcademicRecords();
            setUpComprehensiveExam();
            setUpGraduation();
        }
    }

    private void setUpCommon() {
        applicationNumber = (TextView) findViewById(R.id.view_application_number);
        status = (TextView) findViewById(R.id.view_status);
        approvalLevel = (TextView) findViewById(R.id.view_approval_level);
        dateRequested = (TextView) findViewById(R.id.view_date_requested);
        schoolYearSemester = (TextView) findViewById(R.id.view_school_year_semester);

        applicationNumber.setText("");
        status.setText("");
        approvalLevel.setText("");
        dateRequested.setText("");
        schoolYearSemester.setText("");

        setUpButtons();
    }

    private void setUpButtons() {
        cancelBtn = (Button) findViewById(R.id.cancel_button);
        approveBtn = (Button) findViewById(R.id.approve_button);
        declineBtn = (Button) findViewById(R.id.decline_button);

        sharedPreferences = this.getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);

        String role = sharedPreferences.getString("role", "");

        // STATUS NG APPLICATION, HUWAG KA MAGLALABAS NG BUTTON KAPAG CANCELLED O APPROVED NA

        if (!role.equals("") && role.equals("STUDENT")) {
            cancelBtn.setVisibility(View.VISIBLE);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queue.add(constructStringRequest(appType, "Cancelled"));
                }
            });

        } else if (!role.equals("") && !role.equals("STUDENT")) {
            approveBtn.setVisibility(View.VISIBLE);
            declineBtn.setVisibility(View.VISIBLE);

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queue.add(constructStringRequest(appType, "Approved"));
                }
            });

            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queue.add(constructStringRequest(appType, "Declined"));
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

        if (action.equals("CANCEL")) {
            return url + "cancelApplication/";
        } else {
            return url + "reviewApplication/";
        }
    }

    private void setUpAddSubject() {
        setContentView(R.layout.activity_view_add_subject);
        setUpCommon();

        TextView numberOfSubjects = (TextView) findViewById(R.id.view_as_number_of_subjects);
        TextView listOfSubjects = (TextView) findViewById(R.id.view_as_list_of_subjects);
        TextView reason = (TextView) findViewById(R.id.view_as_reason);

        numberOfSubjects.setText("");
        listOfSubjects.setText("");
        reason.setText("");
    }

    private void setUpChangeSubject() {
        setContentView(R.layout.activity_view_change_subject);
        setUpCommon();

        TextView numberOfSubjects = (TextView) findViewById(R.id.view_as_number_of_subjects);
        TextView listOfFromSubjects = (TextView) findViewById(R.id.view_cs_list_of_original_subjects);
        TextView listOfToSubjects = (TextView) findViewById(R.id.view_cs_list_of_new_subjects);
        TextView reason = (TextView) findViewById(R.id.view_cs_reason);

        numberOfSubjects.setText("");
        listOfFromSubjects.setText("");
        listOfToSubjects.setText("");
        reason.setText("");
    }

    private void setUpDropSubject() {
        setContentView(R.layout.activity_view_drop_subject);
        setUpCommon();

        TextView numberOfSubjects = (TextView) findViewById(R.id.view_ds_number_of_subjects);
        TextView listOfSubjects = (TextView) findViewById(R.id.view_ds_list_of_subjects);
        TextView reason = (TextView) findViewById(R.id.view_ds_reason);

        numberOfSubjects.setText("");
        listOfSubjects.setText("");
        reason.setText("");
    }

    private void setUpOverloadSubject() {
        setContentView(R.layout.activity_view_overload_subject);
        setUpCommon();

        TextView numberOfSubjects = (TextView) findViewById(R.id.view_os_number_of_subjects);
        TextView listOfSubjects = (TextView) findViewById(R.id.view_os_list_of_subjects);
        TextView studentStatus = (TextView) findViewById(R.id.view_os_student_status);
        TextView reason = (TextView) findViewById(R.id.view_os_reason);

        numberOfSubjects.setText("");
        listOfSubjects.setText("");
        studentStatus.setText("");
        reason.setText("");
    }

    private void setUpLeaveOfAbsence() {
        setContentView(R.layout.activity_view_leave_of_absence);
        setUpCommon();

        TextView dateOfEffectivity = (TextView) findViewById(R.id.view_loa_date_of_effectivity);
        TextView reason = (TextView) findViewById(R.id.view_loa_reason);

        dateOfEffectivity.setText("");
        reason.setText("");
    }

    private void setUpCompletion() {
        setContentView(R.layout.activity_view_completion);
        setUpCommon();

        TextView type = (TextView) findViewById(R.id.view_completion_type);
        TextView subject = (TextView) findViewById(R.id.view_completion_subject);
        TextView issue = (TextView) findViewById(R.id.view_completion_issue);
        TextView creditedAs = (TextView) findViewById(R.id.view_completion_credited_as);
        TextView details = (TextView) findViewById(R.id.view_completion_details);
        TextView reason = (TextView) findViewById(R.id.view_completion_reason);

        type.setText("");
        subject.setText("");
        issue.setText("");
        creditedAs.setText("");
        details.setText("");
        reason.setText("");
    }

    private void setUpPetitionTutorialClasses() {
        setContentView(R.layout.activity_view_petition_tutorial_class);
        setUpCommon();

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
        setUpCommon();

        TextView requestedDocumentsCount = (TextView) findViewById(R.id.view_ar_record_requested_count);
        TextView requestedDocuments = (TextView) findViewById(R.id.view_ar_requested_documents);
        TextView reason = (TextView) findViewById(R.id.view_ar_reason);

        requestedDocumentsCount.setText("");
        requestedDocuments.setText("");
        reason.setText("");
    }

    private void setUpComprehensiveExam() {
        setContentView(R.layout.activity_view_comprehensive_exam);
        setUpCommon();

        TextView completedSubjects = (TextView) findViewById(R.id.view_compre_completed_subjects);
        TextView currentlyEnrolledSubjects = (TextView) findViewById(R.id.view_compre_currently_enrolled_subjects);
        TextView reason = (TextView) findViewById(R.id.view_compre_reason);
        TextView totalNumberOfUnits = (TextView) findViewById(R.id.view_compre_total_number_of_units);

        completedSubjects.setText("");
        currentlyEnrolledSubjects.setText("");
        totalNumberOfUnits.setText("");
        reason.setText("");
    }

    private void setUpGraduation() {
        setContentView(R.layout.activity_view_graduation);
        setUpCommon();

        TextView alreadyTakenSubjects = (TextView) findViewById(R.id.view_graduation_already_taken_subjects);
        TextView comprehensiveExamDate = (TextView) findViewById(R.id.view_graduation_compre_exam);
        TextView currentlyEnrolledSubjects = (TextView) findViewById(R.id.view_graduation_currently_enrolled_subjects);
        TextView oralDefenseDate = (TextView) findViewById(R.id.view_graduation_oral_defense_date);
        TextView reason = (TextView) findViewById(R.id.view_graduation_reason);

        alreadyTakenSubjects.setText("");
        comprehensiveExamDate.setText("");
        currentlyEnrolledSubjects.setText("");
        oralDefenseDate.setText("");
        reason.setText("");
    }
}
