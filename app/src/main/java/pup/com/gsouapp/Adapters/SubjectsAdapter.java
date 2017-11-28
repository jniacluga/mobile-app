package pup.com.gsouapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pup.com.gsouapp.Domain.Schedule;
import pup.com.gsouapp.R;

public class SubjectsAdapter extends BaseAdapter
implements ChoiceAdapter, ListAdapter{

    List<Schedule> schedules = new ArrayList();
    List<Boolean> checkboxState;
    LayoutInflater inflater;
    Context context;

    public SubjectsAdapter(Context context, List<Schedule> schedules, List<Boolean> checkboxState) {
        this.schedules = schedules;
        this.context = context;
        this.checkboxState = checkboxState;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Schedule getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final SubjectsAdapter.SubjectViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subject_row, parent, false);
            mViewHolder = new SubjectsAdapter.SubjectViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (SubjectsAdapter.SubjectViewHolder) convertView.getTag();
        }

        Schedule schedule = schedules.get(position);

        mViewHolder.txtVwSubject.setText(schedule.getSubjectCode());
        mViewHolder.chkBxSubject.setChecked(checkboxState.get(position));
        mViewHolder.chkBxSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxState.set(position, mViewHolder.chkBxSubject.isChecked());
            }
        });

        return convertView;
    }

    public List<Boolean> getCheckboxState() {
        return checkboxState;
    }

    private class SubjectViewHolder {
        CheckBox chkBxSubject;
        TextView txtVwSubject;

        public SubjectViewHolder(View item) {
            chkBxSubject = (CheckBox) item.findViewById(R.id.chkBxSubject);
            txtVwSubject = (TextView) item.findViewById(R.id.txtVwSubject);
        }
    }

}
