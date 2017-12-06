package pup.com.gsouapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import pup.com.gsouapp.R;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
        final Intent intent;

        if (!sharedPreferences.getString("sourceId", "").equals("")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else {

            setContentView(R.layout.activity_splash_screen);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}
