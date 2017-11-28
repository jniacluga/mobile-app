package pup.com.gsouapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pup.com.gsouapp.Domain.Subject;
import pup.com.gsouapp.R;

// TODO: CREATE AN INTERFACE AND CREATE A PROPER ADAPTER
public class SubjectsAdapter_2 extends BaseAdapter
implements ChoiceAdapter{

    List<Subject> subjects = new ArrayList<>();
    List<Boolean> checkboxState;
    LayoutInflater inflater;
    Context context;

    public SubjectsAdapter_2(Context context, List<Subject> subjects, List<Boolean> checkboxState) {
        this.subjects = subjects;
        this.context = context;
        this.checkboxState = checkboxState;
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

        final SubjectsAdapter_2.SubjectViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subject_row, parent, false);
            mViewHolder = new SubjectsAdapter_2.SubjectViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (SubjectsAdapter_2.SubjectViewHolder) convertView.getTag();
        }

        Subject subject = subjects.get(position);

        mViewHolder.txtVwSubject.setText(subject.getCode());
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
