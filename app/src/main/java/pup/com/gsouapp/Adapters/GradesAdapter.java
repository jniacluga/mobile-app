package pup.com.gsouapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pup.com.gsouapp.Domain.Grade;
import pup.com.gsouapp.Domain.GradePerSySem;
import pup.com.gsouapp.R;

public class GradesAdapter extends BaseAdapter {

    List<GradePerSySem> myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public GradesAdapter(Context context, List<GradePerSySem> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public GradePerSySem getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GradeViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grade_row, parent, false);
            mViewHolder = new GradeViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (GradeViewHolder) convertView.getTag();
        }

        GradePerSySem gradePerSySem = getItem(position);

        mViewHolder.txtVwSySem.setText(gradePerSySem.getSySem());

        for (Grade grade : gradePerSySem.getGradeList()) {
            TableRow row = new TableRow(context);

            LinearLayout layoutSubject = new LinearLayout(context);
            layoutSubject.setOrientation(LinearLayout.VERTICAL);

            TextView subjectCode = new TextView(context);
            TextView description = new TextView(context);
            TextView faculty = new TextView(context);
            TextView units = new TextView(context);
            TextView sectionCode = new TextView(context);
            TextView finalGrade = new TextView(context);
            TextView status = new TextView(context);

            subjectCode.setText(grade.getSubjectCode());
            description.setText(grade.getDescription());
            faculty.setText(grade.getFaculty());
            units.setText(grade.getUnits());
            sectionCode.setText(grade.getSectionCode());
            finalGrade.setText(grade.getFinalGrade());
            status.setText(grade.getStatus());

            layoutSubject.addView(subjectCode);
            layoutSubject.addView(description);
            layoutSubject.addView(faculty);
            layoutSubject.addView(units);
            layoutSubject.addView(sectionCode);

            row.addView(layoutSubject);
            row.addView(finalGrade);
            row.addView(status);

            mViewHolder.tblGrades.addView(row);
        }

        return convertView;

    }

    private class GradeViewHolder {
        TextView txtVwSySem;
        TableLayout tblGrades;

        public GradeViewHolder(View item) {
            txtVwSySem = (TextView) item.findViewById(R.id.txtVwSySem);
            tblGrades = (TableLayout) item.findViewById(R.id.tblGrades);

        }
    }
}
