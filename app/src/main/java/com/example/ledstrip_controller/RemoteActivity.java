package com.example.ledstrip_controller;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ledstrip_controller.database.Command;
import com.example.ledstrip_controller.database.CommandDao;
import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RemoteActivity extends AppCompatActivity {
    Remote mRemote;
    private RemoteDao mRemoteDao;
    private CommandDao mCommandDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    List<Command> mCommandList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);


        Intent intent = getIntent();
        String mRemoteID = intent.getStringExtra("remoteID");
        int mRemoteIDAsInt = Integer.parseInt(mRemoteID);

        getCommands(mRemoteIDAsInt);

    }

    private void createUI(final Remote mRemote) {

        runOnUiThread(new Runnable() {
            public void run() {

                TextView RemoteNameField = findViewById(R.id.RemoteNameField);
                RemoteNameField.setText(mRemote.mName);

                Button OnButton = findViewById(R.id.OnButton);
                OnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCommand(mRemote.mBaseLink, "/settings?state=1");
                    }
                });

                Button OffButton = findViewById(R.id.OffButton);
                OffButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCommand(mRemote.mBaseLink, "/settings?state=2");
                    }
                });

                LinearLayout ll_main = findViewById(R.id.VerticalLayout);

                LinearLayout parent = null;
                
                for (int i = 0; i < mCommandList.size(); i++) {
                    final Command command = mCommandList.get(i);

                    if (i%3 == 0){
                        parent = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 1;
                        parent.setLayoutParams(param);
                        parent.setOrientation(LinearLayout.HORIZONTAL);
                    }



                    Button myButton = new Button(getApplicationContext());
                    myButton.setText(command.name);
                    myButton.setBackgroundColor(command.color);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendCommand(mRemote.mBaseLink, command.url);
                        }
                    });

                    parent.addView(myButton);


                    if (i % 3 == 0) {
                        ll_main.addView(parent);
                    }
                }
            }
        });


    }

    private void sendCommand(String mBaseURL, String mCommandURL) {

        String commmand = ("http://" + mBaseURL + mCommandURL);
        new GetUrlContentTask().execute(commmand);

        //  KlikGeluid
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.clicking_sound_effect);
        mp.start();
    }

    private void getCommands(final int mRemoteIDAsInt) {
        mExecutor.execute(new Runnable() {

            @Override

            public void run() {
                mCommandDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getCommandDao();

                mCommandList = mCommandDao.findCommandsForRemote(mRemoteIDAsInt);


                mRemoteDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getRemoteDao();

                List<Remote> mRemoteList = mRemoteDao.getRemote(mRemoteIDAsInt);
                mRemote = mRemoteList.get(0);
                createUI(mRemote);

            }
        });
    }


}

