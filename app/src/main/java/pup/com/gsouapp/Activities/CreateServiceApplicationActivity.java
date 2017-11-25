package pup.com.gsouapp.Activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import pup.com.gsouapp.R;
import pup.com.gsouapp.ServiceApplicationFragments.AcademicRecords;
import pup.com.gsouapp.ServiceApplicationFragments.AddSubject;
import pup.com.gsouapp.ServiceApplicationFragments.ChangeSubject;
import pup.com.gsouapp.ServiceApplicationFragments.Completion;
import pup.com.gsouapp.ServiceApplicationFragments.ComprehensiveExam;
import pup.com.gsouapp.ServiceApplicationFragments.DropSubject;
import pup.com.gsouapp.ServiceApplicationFragments.Graduation;
import pup.com.gsouapp.ServiceApplicationFragments.LeaveOfAbsence;
import pup.com.gsouapp.ServiceApplicationFragments.OverloadSubject;
import pup.com.gsouapp.ServiceApplicationFragments.PetitionTutorialClass;
import pup.com.gsouapp.SubjectChecklist;

public class CreateServiceApplicationActivity extends AppCompatActivity
    implements AddSubject.OnFragmentInteractionListener,
                ChangeSubject.OnFragmentInteractionListener,
                DropSubject.OnFragmentInteractionListener,
                OverloadSubject.OnFragmentInteractionListener,
                Completion.OnFragmentInteractionListener,
                ComprehensiveExam.OnFragmentInteractionListener,
                PetitionTutorialClass.OnFragmentInteractionListener,
                Graduation.OnFragmentInteractionListener,
                LeaveOfAbsence.OnFragmentInteractionListener,
                AcademicRecords.OnFragmentInteractionListener,
                SubjectChecklist.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service_application);

        final Spinner spinner = (Spinner) findViewById(R.id.service_application_option);

        AddSubject addSubject = new AddSubject();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.service_application_layout, addSubject).commit();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment;

                String type = spinner.getItemAtPosition(position).toString();

                switch (type) {
                    case "Add Subject":
                        fragment = new AddSubject();
                        break;
                    case "Change Subject":
                        fragment = new ChangeSubject();
                        break;
                    case "Drop Subject":
                        fragment = new DropSubject();
                        break;
                    case "Overload Subject":
                        fragment = new OverloadSubject();
                        break;
                    case "Completion":
                        fragment = new Completion();
                        break;
                    case "Leave of Absence":
                        fragment = new LeaveOfAbsence();
                        break;
                    case "Comprehensive Exam":
                        fragment = new ComprehensiveExam();
                        break;
                    case "Petition/Tutorial Class":
                        fragment = new PetitionTutorialClass();
                        break;
                    case "Academic Records":
                        fragment = new AcademicRecords();
                        break;
                    case "Graduation":
                        fragment = new Graduation();
                        break;
                    default:
                        fragment = new AddSubject();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.service_application_layout, fragment);
                transaction.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
