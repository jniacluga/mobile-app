package pup.com.gsouapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import pup.com.gsouapp.Adapters.FragmentCollectionPagerAdapter;
import pup.com.gsouapp.MainFragments.GradesFragment;
import pup.com.gsouapp.MainFragments.HomeFragment;
import pup.com.gsouapp.MainFragments.SchedulesFragment;
import pup.com.gsouapp.MainFragments.ServiceApplicationApproverFragment;
import pup.com.gsouapp.MainFragments.ServiceApplicationFragment;
import pup.com.gsouapp.MainFragments.ThesisDissertationFragment;
import pup.com.gsouapp.R;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        SchedulesFragment.OnFragmentInteractionListener,
        GradesFragment.OnFragmentInteractionListener,
        ServiceApplicationFragment.OnFragmentInteractionListener,
        ServiceApplicationApproverFragment.OnFragmentInteractionListener,
        ThesisDissertationFragment.OnFragmentInteractionListener {

    FragmentCollectionPagerAdapter pageCollectionAdapter;
    ViewPager viewPager;

    Toolbar toolbar;
    TabLayout tabLayout;

    int[] tabIcons = {
            R.drawable.gsou_logo_1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int position = extras.getInt("selectedPage");
            viewPager.setCurrentItem(position);
        }
    }

    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(3).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(4).setIcon(tabIcons[0]);
    }

    private void setupViewPager(ViewPager viewPager) {
        pageCollectionAdapter = new FragmentCollectionPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        pageCollectionAdapter.addFragment(HomeFragment.getInstance(), "H");
        pageCollectionAdapter.addFragment(SchedulesFragment.getInstance(), "S");
        pageCollectionAdapter.addFragment(GradesFragment.getInstance(), "G");
        pageCollectionAdapter.addFragment(ServiceApplicationFragment.getInstance(), "A");
        pageCollectionAdapter.addFragment(ThesisDissertationFragment.getInstance(), "T");
        viewPager.setAdapter(pageCollectionAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
