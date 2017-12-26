package pup.com.gsouapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pup.com.gsouapp.MainFragments.GradesFragment;
import pup.com.gsouapp.MainFragments.HomeFragment;
import pup.com.gsouapp.MainFragments.SchedulesFragment;
import pup.com.gsouapp.MainFragments.ServiceApplicationApproverFragment;
import pup.com.gsouapp.MainFragments.ServiceApplicationFragment;
import pup.com.gsouapp.MainFragments.ThesisDissertationFragment;

public class FragmentCollectionPagerAdapter extends FragmentPagerAdapter {

    final private int HOME_INT = 0;
    final private int SCHEDULES_INT = 1;
    final private int GRADES_INT = 2;
    final private int SERVICE_APPLICATION_INT = 3;
    final private int THESIS_DISSERTATION_INT = 4;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    boolean isStudent;

    public FragmentCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentCollectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginCredentials",Context.MODE_PRIVATE);
        isStudent = sharedPreferences.getString("role", "").equals("STUDENT");
    }

    @Override
    public Fragment getItem(int position) {
        if (!isStudent && position == 1) {
            return mFragmentList.get(3);
        }
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return isStudent ? mFragmentList.size() : 2;
    }

    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
//        return null;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
}
