package pup.com.gsouapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import pup.com.gsouapp.Adapters.FragmentCollectionPagerAdapter;
import pup.com.gsouapp.MainFragments.GradesFragment;
import pup.com.gsouapp.MainFragments.HomeFragment;
import pup.com.gsouapp.MainFragments.SchedulesFragment;
import pup.com.gsouapp.MainFragments.ServiceApplicationFragment;
import pup.com.gsouapp.MainFragments.ThesisDissertationFragment;
import pup.com.gsouapp.R;

public class MainActivity extends FragmentActivity
        implements HomeFragment.OnFragmentInteractionListener,
        SchedulesFragment.OnFragmentInteractionListener,
        GradesFragment.OnFragmentInteractionListener,
        ServiceApplicationFragment.OnFragmentInteractionListener,
        ThesisDissertationFragment.OnFragmentInteractionListener {

    FragmentCollectionPagerAdapter pageCollectionAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pageCollectionAdapter = new FragmentCollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pageCollectionAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("MainActivity", uri.toString());
    }
}
