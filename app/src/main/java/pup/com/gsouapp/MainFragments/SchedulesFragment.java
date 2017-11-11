package pup.com.gsouapp.MainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pup.com.gsouapp.R;
import pup.com.gsouapp.Domain.Schedule;
import pup.com.gsouapp.Adapters.SchedulesAdapter;
import pup.com.gsouapp.Domain.Semester;
import pup.com.gsouapp.Domain.Sy;

public class SchedulesFragment extends Fragment {

    final String SUNDAY = "SUNDAYS";
    final String MONDAY = "MONDAY";
    final String TUESDAY = "TUESDAY";
    final String WEDNESDAY = "WEDNESDAY";
    final String THURSDAY = "THURSDAY";
    final String FRIDAY = "FRIDAY";
    final String SATURDAY = "SATURDAY";

    RequestQueue queue;
    List<Semester> semesterList = new ArrayList<>();
    List<Sy> syList = new ArrayList<>();

    List<Schedule> sundaySchedules = new ArrayList<>();
    List<Schedule> mondaySchedules = new ArrayList<>();
    List<Schedule> tuesdaySchedules = new ArrayList<>();
    List<Schedule> wednesdaySchedules = new ArrayList<>();
    List<Schedule> thursdaySchedules = new ArrayList<>();
    List<Schedule> fridaySchedules = new ArrayList<>();
    List<Schedule> saturdaySchedules = new ArrayList<>();

    Button btnSunday;
    Button btnMonday;
    Button btnTuesday;
    Button btnWednesday;
    Button btnThursday;
    Button btnFriday;
    Button btnSaturday;

    View view;

    ListView listView;

    Spinner spinnerSy;
    Spinner spinnerSem;

    private OnFragmentInteractionListener mListener;

    public SchedulesFragment() {
        // Required empty public constructor
    }

    public static SchedulesFragment getInstance() {
        SchedulesFragment fragment = new SchedulesFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_schedules, container, false);

        /*queue = Volley.newRequestQueue(getContext());

        spinnerSy = (Spinner) view.findViewById(R.id.spinnerSchoolYear);
        spinnerSem = (Spinner) view.findViewById(R.id.spinnerSemester);

        btnSunday = (Button) view.findViewById(R.id.btnSunday);
        btnMonday = (Button) view.findViewById(R.id.btnMonday);
        btnTuesday = (Button) view.findViewById(R.id.btnTuesday);
        btnWednesday = (Button) view.findViewById(R.id.btnWednesday);
        btnThursday = (Button) view.findViewById(R.id.btnThursday);
        btnFriday = (Button) view.findViewById(R.id.btnFriday);
        btnSaturday = (Button) view.findViewById(R.id.btnSaturday);

        getSchoolYear();
        getSemester();

        spinnerSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadSchedule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        spinnerSy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadSchedule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(sundaySchedules);
            }
        });

        btnMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(mondaySchedules);
            }
        });

        btnTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(tuesdaySchedules);
            }
        });

        btnWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(wednesdaySchedules);
            }
        });

        btnThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(thursdaySchedules);
            }
        });

        btnFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(fridaySchedules);
            }
        });

        btnSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindSchedule(saturdaySchedules);
            }
        });*/

        return view;
    }

    public void getSchoolYear() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.get_school_years), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {

                        syList.clear();

                        JSONArray arr = new JSONArray(response);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            Sy sy = new Sy(
                                obj.getLong("id"),
                                obj.getString("abbr"),
                                obj.getString("start"),
                                obj.getString("end")
                            );

                            syList.add(sy);

                            bindSchoolYear();
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
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
                params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
                return params;
            }
        };

        queue.add(request);
    }

    public void getSemester() {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.get_semesters), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {

                        semesterList.clear();

                        JSONArray arr = new JSONArray(response);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            Semester semester = new Semester(
                                    obj.getLong("id"),
                                    obj.getString("semester")
                            );

                            semesterList.add(semester);
                        }

                        bindSemester();

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
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
                params.put("studentNumber", sharedPreferences.getString("sourceId", ""));
                return params;
            }
        };

        queue.add(request);
    }

    private void bindSchoolYear() {

        ArrayAdapter<Sy> adapter = new ArrayAdapter<Sy>(getContext(), R.layout.support_simple_spinner_dropdown_item, syList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerSy.setAdapter(adapter);
    }

    private void bindSemester() {

        ArrayAdapter<Semester> adapter = new ArrayAdapter<Semester>(getContext(), R.layout.support_simple_spinner_dropdown_item, semesterList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerSem.setAdapter(adapter);

    }

    public void loadSchedule() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.load_schedule), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {

                        sundaySchedules.clear();
                        mondaySchedules.clear();
                        tuesdaySchedules.clear();
                        wednesdaySchedules.clear();
                        thursdaySchedules.clear();
                        fridaySchedules.clear();
                        saturdaySchedules.clear();

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
                                obj.getString("description"),
                                obj.getString("faculty"),
                                obj.getString("room"),
                                obj.getString("sectionCode")
                            );

                            switch(obj.getString("day")) {
                                case SUNDAY:
                                    sundaySchedules.add(schedule);
                                    break;
                                case MONDAY:
                                    mondaySchedules.add(schedule);
                                    break;
                                case TUESDAY:
                                    tuesdaySchedules.add(schedule);
                                    break;
                                case WEDNESDAY:
                                    wednesdaySchedules.add(schedule);
                                    break;
                                case THURSDAY:
                                    thursdaySchedules.add(schedule);
                                    break;
                                case FRIDAY:
                                    fridaySchedules.add(schedule);
                                    break;
                                case SATURDAY:
                                    saturdaySchedules.add(schedule);
                                    break;
                                default:
                                    break;
                            }
                        }

                        bindSchedule(sundaySchedules);

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

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
                params.put("studentNumber", sharedPreferences.getString("sourceId", ""));

                Sy sy = (Sy) spinnerSy.getSelectedItem();
                Semester semester =  (Semester) spinnerSem.getSelectedItem();
                params.put("sy", sy.getId().toString());
                params.put("sem", semester.getId().toString());

                return params;
            }
        };

        queue.add(request);
    }

    private void bindSchedule(List<Schedule> schedules) {

        listView = (ListView) view.findViewById(R.id.lvSchedule);
        listView.setAdapter(new SchedulesAdapter(getContext(), schedules));
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
