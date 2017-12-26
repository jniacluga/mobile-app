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

        Schedule schedule = myList.get(position);

//        mViewHolder.txtVwSy.setText(schedule.getSy());
//        mViewHolder.txtVwSem.setText(schedule.getSem());
        mViewHolder.txtVwDay.setText(schedule.getDay());
        mViewHolder.txtVwTime.setText(schedule.getStartTime() + " - " + schedule.getEndTime());
//        mViewHolder.txtVwEndTime.setText(schedule.getEndTime());
        mViewHolder.txtVwSubject.setText(schedule.getSubjectCode() + " " + schedule.getDescription());
//        mViewHolder.txtVwDescription.setText(schedule.getDescription());
        mViewHolder.txtVwFaculty.setText(schedule.getFaculty());
        mViewHolder.txtVwRoom.setText(schedule.getRoom());
        mViewHolder.txtVwSectionCode.setText(schedule.getSectionCode());

        return convertView;

    }

    private class ScheduleViewHolder {
//        TextView txtVwSy;
//        TextView txtVwSem;
        TextView txtVwDay;
        TextView txtVwTime;
//        TextView txtVwEndTime;
        TextView txtVwSubject;
//        TextView txtVwDescription;
        TextView txtVwFaculty;
        TextView txtVwRoom;
        TextView txtVwSectionCode;

        public ScheduleViewHolder(View item) {
//            txtVwSy = (TextView) item.findViewById(R.id.txtVwSy);
//            txtVwSem = (TextView) item.findViewById(R.id.txtVwSem);
            txtVwDay = (TextView) item.findViewById(R.id.txtVwDay);
            txtVwTime = (TextView) item.findViewById(R.id.txtVwTime);
//            txtVwEndTime = (TextView) item.findViewById(R.id.txtVwEndTime);
            txtVwSubject = (TextView) item.findViewById(R.id.txtVwSubject);
//            txtVwDescription = (TextView) item.findViewById(R.id.txtVwDescription);
            txtVwFaculty = (TextView) item.findViewById(R.id.txtVwFaculty);
            txtVwRoom = (TextView) item.findViewById(R.id.txtVwRoom);
            txtVwSectionCode = (TextView) item.findViewById(R.id.txtVwSectionCode);
        }
    }

}
