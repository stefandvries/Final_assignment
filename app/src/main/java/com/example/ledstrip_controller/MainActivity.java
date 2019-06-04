package com.example.ledstrip_controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ledstrip_controller.database.CommandDao;
import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private int state = 0;
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    Remote mRemote;
    private RemoteDao mRemoteDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create ShakeListener
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                controlFavRemote();
            }
        });

        // Stuff for fragments
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RemotesFragment()).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_remotes:
                            selectedFragment = new RemotesFragment();
                            break;

                        case R.id.nav_favorites:
                            selectedFragment = new FavoriteFragment();
                            break;

                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    public void controlFavRemote() {
        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getRemoteDao();

                List<Remote> mRemoteList = mRemoteDao.getFavorite();

                String commmand;

                if (mRemoteList.size() == 0) {
                } else {

                    mRemote = mRemoteList.get(0);

                    if (state == 0) {
                        // turn on
                        commmand = ("http://" + mRemote.mBaseLink + "/settings?state=1");
                        Log.d("Tag", mRemote.mBaseLink);
                        state = 1;
                    } else {
                        // turn off
                        commmand = ("http://" + mRemote.mBaseLink + "/settings?state=2");
                        state = 0;
                    }

                    new com.example.ledstrip_controller.GetUrlContentTask().execute(commmand);

                    //  KlikGeluid
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.clicking_sound_effect);
                    mp.start();

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register listener again after resume of app
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Unregister listener after app gets paused
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
