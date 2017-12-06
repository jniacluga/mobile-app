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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

import pup.com.gsouapp.Domain.Defense;
import pup.com.gsouapp.Domain.Evaluator;
import pup.com.gsouapp.Helpers.VolleyClass;
import pup.com.gsouapp.R;

public class ThesisDissertationFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private boolean studentThesisEligible = false;
    private String thesisStatus = "";

    private TextView txtVwYet;

    private LinearLayout layoutButtons;
    private LinearLayout layoutProposal;
    private LinearLayout layoutPreoral;
    private LinearLayout layoutOral;
    private LinearLayout layoutRegistration;
    private LinearLayout layoutComplete;

    private Button btnRegistration;
    private Button btnProposalDefense;
    private Button btnPreoralDefense;
    private Button btnOralDefense;
    private Button btnComplete;

    private List<Defense> defenseList =  new ArrayList<>();
    private List<Evaluator> evaluators = new ArrayList<>();

    private static final String PROPOSAL_DEFENSE = "Proposal Defense";
    private static final String PREORAL_DEFENSE = "Pre-oral Defense";
    private static final String ORAL_DEFENSE = "Oral Defense";

    RequestQueue queue;

    public ThesisDissertationFragment() {

    }

    public static ThesisDissertationFragment getInstance() {
        ThesisDissertationFragment fragment = new ThesisDissertationFragment();
        return fragment;
    }

    public static ThesisDissertationFragment newInstance() {
        ThesisDissertationFragment fragment = new ThesisDissertationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_thesis_dissertation, container, false);

        txtVwYet = (TextView) view.findViewById(R.id.txtVwYet);

        queue = VolleyClass.getInstance(getContext()).getRequestQueue();

        getStudentEligibility();
        getPaperStatus(view);

        return view;
    }

    private void getStudentEligibility() {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.student_thesis_eligibility), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    if (response.contains("1")) {
                        studentThesisEligible = true;
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

    private void getPaperStatus(final View view) {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.paper_status), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    if (response.contains("Approved")) {
                        thesisStatus = "Approved";
                    } else if (response.contains("For Approval")) {
                        thesisStatus = "For Approval";
                    }

                    populateLists(view);
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

    private void populateLists(final View view) {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.web_server) +
                getResources().getString(R.string.thesis_status), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("\r\n\"\"")) {

                    try {

                        if (evaluators != null && defenseList != null) {

                            evaluators.clear();
                            defenseList.clear();

                            JSONArray arr = new JSONArray(response);
                            Evaluator evaluator;
                            Defense defense = new Defense();
                            Double totalPayment = 0D;
                            String level = "";

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                totalPayment += Double.parseDouble(obj.getString("payment"));

                                defense.setLevel(obj.getString("level"));
                                defense.setDateTime(obj.getString("dateTime"));
                                defense.setDay(obj.getString("day"));
                                defense.setStatus(obj.getString("status"));

                                evaluator = new Evaluator(obj.getString("evaluatorType"),
                                        obj.getString("evaluator"),
                                        Double.parseDouble(obj.getString("payment")));
                                evaluators.add(evaluator);

                                if (!level.equals("") && (!obj.getString("level").equals(level) || i == arr.length() - 1)) {
                                    defense.setTotalPayment(totalPayment);
                                    defense.setEvaluators(evaluators);

                                    defenseList.add(defense);

                                    totalPayment = 0D;
                                    evaluators = null;
                                    defense = new Defense();
                                }

                                level = obj.getString("level");
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                initializeViews(view);
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

    private void initializeViews(View view) {

        TextView txtVwRegistration = (TextView) view.findViewById(R.id.txtVwRegistrationYet);
        if (defenseList.size() > 0 || thesisStatus.equals("Approved")) {
            txtVwRegistration.setText("Your thesis/dissertation is already approved and registered!\n" +
                    "YOU CAN START MONITORING YOUR PROGRESS IN THE MONITORING PAGE.");
        } else if (thesisStatus.equals("For Approval")) {
            txtVwRegistration.setText("Your thesis/dissertation is set for approval.");
        }

        if (defenseList.size() > 0) {

            for (int i=0; i<defenseList.size(); i++) {

                String level = defenseList.get(i).getLevel();
                List<Evaluator> evaluators = defenseList.get(i).getEvaluators();

                TextView txtVwDefenseLevel = null;
                TextView txtVwDateTime = null;
                TextView txtVwDay = null;
                TableLayout tblCommittee = null;
                TextView txtVwTotalPayment = null;

                if (level.equals(PROPOSAL_DEFENSE)) {
                    txtVwDefenseLevel = (TextView) view.findViewById(R.id.txtVwPropDefenseLevel);
                    txtVwDateTime = (TextView) view.findViewById(R.id.txtVwPropDateTime);
                    txtVwDay = (TextView) view.findViewById(R.id.txtVwPropDay);
                    tblCommittee = (TableLayout) view.findViewById(R.id.tblPropCommittee);
                    txtVwTotalPayment = (TextView) view.findViewById(R.id.txtVwPropTotalPayment);

                } else if (level.equals(PREORAL_DEFENSE)) {
                    txtVwDefenseLevel = (TextView) view.findViewById(R.id.txtVwPreDefenseLevel);
                    txtVwDateTime = (TextView) view.findViewById(R.id.txtVwPreDateTime);
                    txtVwDay = (TextView) view.findViewById(R.id.txtVwPreDay);
                    tblCommittee = (TableLayout) view.findViewById(R.id.tblPreCommittee);
                    txtVwTotalPayment = (TextView) view.findViewById(R.id.txtVwPreTotalPayment);
                } else if (level.equals(ORAL_DEFENSE)) {
                    txtVwDefenseLevel = (TextView) view.findViewById(R.id.txtVwOralDefenseLevel);
                    txtVwDateTime = (TextView) view.findViewById(R.id.txtVwOralDateTime);
                    txtVwDay = (TextView) view.findViewById(R.id.txtVwOralDay);
                    tblCommittee = (TableLayout) view.findViewById(R.id.tblOralCommittee);
                    txtVwTotalPayment = (TextView) view.findViewById(R.id.txtVwOralTotalPayment);
                }

                txtVwDefenseLevel.setText(defenseList.get(i).getLevel());
                txtVwDateTime.setText(defenseList.get(i).getDateTime());
                txtVwDay.setText(defenseList.get(i).getDay());

                for (Evaluator evaluator : evaluators) {
                    TableRow row = new TableRow(getContext());
                    TextView evaluatorType = new TextView(getContext());
                    TextView evaluatorName = new TextView(getContext());
                    TextView payment = new TextView(getContext());

                    evaluatorType.setText(evaluator.getEvaluatorType());
                    evaluatorName.setText(evaluator.getEvaluatorName());
                    payment.setText(evaluator.getPayment().toString());

                    row.addView(evaluatorType);
                    row.addView(evaluatorName);
                    row.addView(payment);

                    tblCommittee.addView(row);
                }

                txtVwTotalPayment.setText(defenseList.get(i).getTotalPayment().toString());

                Defense defense = defenseList.get(defenseList.size()-1);
                TextView txtVwComplete = (TextView) view.findViewById(R.id.txtVwCompleteYet);
                if (defense.getStatus().equals("COMPLETE") && defense.getLevel().equals("Oral Defense")) {
                    txtVwComplete.setText("Thesis/Dissertation Writing and Defense has been completed! CLICK HERE TO APPLY FOR GRADUATION");
                }
            }
        }

        layoutButtons = (LinearLayout) view.findViewById(R.id.layoutButtons);
        layoutProposal = (LinearLayout) view.findViewById(R.id.layoutProposal);
        layoutPreoral = (LinearLayout) view.findViewById(R.id.layoutPreoral);
        layoutOral = (LinearLayout) view.findViewById(R.id.layoutOral);
        layoutRegistration = (LinearLayout) view.findViewById(R.id.layoutRegistration);
        layoutComplete = (LinearLayout) view.findViewById(R.id.layoutComplete);

        btnRegistration = (Button) view.findViewById(R.id.btnRegistration);
        btnProposalDefense = (Button) view.findViewById(R.id.btnProposalDefense);
        btnPreoralDefense = (Button) view.findViewById(R.id.btnPreoralDefense);
        btnOralDefense = (Button) view.findViewById(R.id.btnOralDefense);
        btnComplete = (Button) view.findViewById(R.id.btnComplete);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtVwYet.setVisibility(View.GONE);
                layoutRegistration.setVisibility(View.VISIBLE);
                layoutProposal.setVisibility(View.GONE);
                layoutPreoral.setVisibility(View.GONE);
                layoutOral.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.GONE);
            }
        });

        btnProposalDefense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegistration.setVisibility(View.GONE);

                if (defenseList.size() > 0) {
                    layoutProposal.setVisibility(View.VISIBLE);
                    txtVwYet.setVisibility(View.GONE);
                } else {
                    layoutProposal.setVisibility(View.GONE);
                    txtVwYet.setVisibility(View.VISIBLE);
                }

                layoutPreoral.setVisibility(View.GONE);
                layoutOral.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.GONE);
            }
        });

        btnPreoralDefense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegistration.setVisibility(View.GONE);
                layoutProposal.setVisibility(View.GONE);

                if (defenseList.size() > 1) {
                    layoutPreoral.setVisibility(View.VISIBLE);
                    txtVwYet.setVisibility(View.GONE);
                } else {
                    layoutPreoral.setVisibility(View.GONE);
                    txtVwYet.setVisibility(View.VISIBLE);
                }

                layoutOral.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.GONE);
            }
        });

        btnOralDefense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegistration.setVisibility(View.GONE);
                layoutProposal.setVisibility(View.GONE);
                layoutPreoral.setVisibility(View.GONE);

                if (defenseList.size() > 2) {
                    layoutOral.setVisibility(View.VISIBLE);
                    txtVwYet.setVisibility(View.GONE);
                } else {
                    layoutOral.setVisibility(View.GONE);
                    txtVwYet.setVisibility(View.VISIBLE);
                }

                layoutComplete.setVisibility(View.GONE);
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtVwYet.setVisibility(View.GONE);
                layoutRegistration.setVisibility(View.GONE);
                layoutProposal.setVisibility(View.GONE);
                layoutPreoral.setVisibility(View.GONE);
                layoutOral.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.VISIBLE);
            }
        });

        switch (defenseList.size()) {
            case 1:
                btnProposalDefense.performClick();
                break;
            case 2:
                btnPreoralDefense.performClick();
                break;
            case 3:
                btnOralDefense.performClick();

                if (defenseList.get(defenseList.size()-1).getStatus().equals("COMPLETE")) {
                    btnComplete.performClick();
                }
                break;
            default:
                btnRegistration.performClick();
                break;
        }
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
