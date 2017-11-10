package pup.com.gsouapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pup.com.gsouapp.MainFragments.GradesFragment;
import pup.com.gsouapp.MainFragments.HomeFragment;
import pup.com.gsouapp.MainFragments.SchedulesFragment;
import pup.com.gsouapp.MainFragments.ServiceApplicationFragment;
import pup.com.gsouapp.MainFragments.ThesisDissertationFragment;

public class FragmentCollectionPagerAdapter extends FragmentPagerAdapter {

    final private int HOME_INT = 0;
    final private int SCHEDULES_INT = 1;
    final private int GRADES_INT = 2;
    final private int SERVICE_APPLICATION_INT = 3;
    final private int THESIS_DISSERTATION_INT = 4;

    public FragmentCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        switch (position) {
            case HOME_INT:
                fragment = HomeFragment.getInstance();
                break;
            case SCHEDULES_INT:
                fragment = SchedulesFragment.getInstance();
                break;
            case GRADES_INT:
                fragment = GradesFragment.getInstance();
                break;
            case SERVICE_APPLICATION_INT:
                fragment = ServiceApplicationFragment.newInstance();
                break;
            case THESIS_DISSERTATION_INT:
                fragment = ThesisDissertationFragment.newInstance();
                break;
            default:
                fragment = HomeFragment.getInstance();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {

        return 5;
    }

    public CharSequence getPageTitle(int position) {

        switch (position) {
            case HOME_INT:
                return "Home";
            case SCHEDULES_INT:
                return "Schedules";
            case GRADES_INT:
                return "Grades";
            case SERVICE_APPLICATION_INT:
                return "Service Application";
            case THESIS_DISSERTATION_INT:
                return "Thesis Dissertation";
            default:
                return "Home";
        }
    }
}
