package pup.com.gsouapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;

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

//        String url = getResources().getString(R.string.web_server);
//        int timeout = 2;

//        if (isConnectedToServer(url, timeout)) {
            setContentView(R.layout.activity_main);
            pageCollectionAdapter = new FragmentCollectionPagerAdapter(getSupportFragmentManager());
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(pageCollectionAdapter);
//        } else {
//            setContentView(R.layout.no_connection);
//        }
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("MainActivity", uri.toString());
    }
}
