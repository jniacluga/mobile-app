package pup.com.gsouapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pup.com.gsouapp.Domain.Subject;
import pup.com.gsouapp.R;

public class SubjectsAdapterNoCheckbox extends BaseAdapter {

    List<Subject> subjects = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public SubjectsAdapterNoCheckbox(Context context, List<Subject> subjects) {
        this.subjects = subjects;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Subject getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final SubjectsAdapterNoCheckbox.SubjectViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subject_row_no_checkbox, parent, false);
            mViewHolder = new SubjectsAdapterNoCheckbox.SubjectViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (SubjectsAdapterNoCheckbox.SubjectViewHolder) convertView.getTag();
        }

        Subject subject = subjects.get(position);

        mViewHolder.txtVwSubjectCode.setText(subject.getCode());

        return convertView;
    }

    private class SubjectViewHolder {
        TextView txtVwSubjectCode;

        public SubjectViewHolder(View item) {
            txtVwSubjectCode = (TextView) item.findViewById(R.id.txtVwSubjectCode);
        }
    }
}
