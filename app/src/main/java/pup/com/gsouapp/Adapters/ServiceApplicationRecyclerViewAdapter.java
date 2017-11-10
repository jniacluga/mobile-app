package pup.com.gsouapp.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pup.com.gsouapp.R;
import pup.com.gsouapp.Domain.ServiceApplication;
import pup.com.gsouapp.Activities.ViewServiceApplicationActivity;

public class ServiceApplicationRecyclerViewAdapter extends RecyclerView.Adapter<ServiceApplicationRecyclerViewAdapter.CustomViewHolder> {

    private List<ServiceApplication> serviceApplications;

    public ServiceApplicationRecyclerViewAdapter(List<ServiceApplication> serviceApplications) {
        this.serviceApplications = serviceApplications;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_application_row, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final ServiceApplication application = serviceApplications.get(position);

        holder.applicationType.setText(application.getType().toString());
        holder.dateRequest.setText(application.getDateRequested());
        holder.summary.setText(application.getSummary());
        holder.status.setText(application.getStatus().toString());
        holder.applicationNumber.setText(application.getApplicationNo());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewServiceApplicationActivity.class);
                intent.putExtra("appId", application.getId());
                intent.putExtra("appType", application.getType());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (serviceApplications != null ? serviceApplications.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView applicationType;
        protected TextView dateRequest;
        protected TextView summary;
        protected TextView status;
        protected TextView applicationNumber;
        protected Button viewButton;

        public CustomViewHolder(View view) {
            super(view);
            this.applicationType = (TextView) view.findViewById(R.id.application_type);
            this.dateRequest = (TextView) view.findViewById(R.id.date_request);
            this.summary = (TextView) view.findViewById(R.id.summary);
            this.status = (TextView) view.findViewById(R.id.status);
            this.applicationNumber = (TextView) view.findViewById(R.id.application_number);
            this.viewButton = (Button) view.findViewById(R.id.view_button);
        }
    }
}
