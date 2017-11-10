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

import pup.com.gsouapp.Domain.Schedule;
import pup.com.gsouapp.R;

public class SchedulesAdapter extends BaseAdapter {

    List<Schedule> myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    public SchedulesAdapter(Context context, List<Schedule> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Schedule getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.schedule_row, parent, false);
            mViewHolder = new ScheduleViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ScheduleViewHolder) convertView.getTag();
        }

        String scheduleDay = getItem(position).getDay();

        mViewHolder.txtVwDay.setText(scheduleDay);

        for (Schedule schedule : myList) {
            TableRow row = new TableRow(context);

            LinearLayout layoutSchedule = new LinearLayout(context);
            layoutSchedule.setOrientation(LinearLayout.VERTICAL);

            TextView sy = new TextView(context);
            TextView sem = new TextView(context);
            TextView day = new TextView(context);
            TextView startTime = new TextView(context);
            TextView endTime = new TextView(context);
            TextView subjectCode = new TextView(context);
            TextView description = new TextView(context);
            TextView faculty = new TextView(context);
            TextView room = new TextView(context);
            TextView sectionCode = new TextView(context);

            sy.setText(schedule.getSy());
            sem.setText(schedule.getSem());
            day.setText(schedule.getDay());
            startTime.setText(schedule.getStartTime());
            endTime.setText(schedule.getEndTime());
            subjectCode.setText(schedule.getSubjectCode());
            description.setText(schedule.getDescription());
            faculty.setText(schedule.getFaculty());
            room.setText(schedule.getRoom());
            sectionCode.setText(schedule.getSectionCode());

            layoutSchedule.addView(sy);
            layoutSchedule.addView(sem);
            layoutSchedule.addView(day);
            layoutSchedule.addView(startTime);
            layoutSchedule.addView(endTime);
            layoutSchedule.addView(subjectCode);
            layoutSchedule.addView(description);
            layoutSchedule.addView(faculty);
            layoutSchedule.addView(room);
            layoutSchedule.addView(sectionCode);

            row.addView(layoutSchedule);

            mViewHolder.tblSchedule.addView(row);
        }

        return convertView;

    }

    private class ScheduleViewHolder {
        TextView txtVwDay;
        TableLayout tblSchedule;

        public ScheduleViewHolder(View item) {
            txtVwDay = (TextView) item.findViewById(R.id.txtVwDay);
            tblSchedule = (TableLayout) item.findViewById(R.id.tblSchedule);

        }
    }

}
