package pup.com.gsouapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pup.com.gsouapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(this, MainActivity.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);

                if (!sharedPreferences.getString("sourceId", "").equals("")) {
                    startActivity(intent);
                } else {
                    setContentView(R.layout.activity_splash_screen);
                }
            }
        }, 2500);
    }
}
