package com.example.ledstrip_controller;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ledstrip_controller.database.Command;
import com.example.ledstrip_controller.database.CommandDao;
import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoriteFragment extends Fragment {


    Remote mRemote;
    private RemoteDao mRemoteDao;
    private CommandDao mCommandDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    List<Command> mCommandList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View myFragmentView = inflater.inflate(R.layout.fragment_favorite, container, false);

        startCreatingUI();

        return myFragmentView;
    }

    public void startCreatingUI() {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao = RepoDatabase
                        .getInstance(getContext())
                        .getRemoteDao();

                List<Remote> mRemoteList = mRemoteDao.getFavorite();

                for (int i = 0; i < mRemoteList.size(); i++) {
                }

                if (mRemoteList.size() == 0) {
                } else {
                    mRemote = mRemoteList.get(0);
                    getCommands(mRemote.id);
                }
            }
        });

    }


    private void createUI(final Remote mRemote) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                TextView RemoteNameField = getActivity().findViewById(R.id.RemoteNameField);
                RemoteNameField.setText(mRemote.mName);

                Button OnButton = getActivity().findViewById(R.id.OnButton);
                OnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCommand(mRemote.mBaseLink, "/settings?state=1");
                    }
                });

                Button OffButton = getActivity().findViewById(R.id.OffButton);
                OffButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCommand(mRemote.mBaseLink, "/settings?state=2");
                    }
                });

                LinearLayout ll_main = getActivity().findViewById(R.id.VerticalLayout);

                LinearLayout parent = null;

                for (int i = 0; i < mCommandList.size(); i++) {
                    final Command command = mCommandList.get(i);

                    if (i % 3 == 0) {
                        parent = new LinearLayout(getContext());
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 1;
                        parent.setLayoutParams(param);
                        parent.setOrientation(LinearLayout.HORIZONTAL);
                    }


                    Button myButton = new Button(getContext());
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

    public void sendCommand(String mBaseURL, String mCommandURL) {

        String commmand = ("http://" + mBaseURL + mCommandURL);
        new com.example.ledstrip_controller.GetUrlContentTask().execute(commmand);

        //  KlikGeluid
        MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.clicking_sound_effect);
        mp.start();
    }

    private void getCommands(final int mRemoteIDAsInt) {
        mExecutor.execute(new Runnable() {

            @Override

            public void run() {
                mCommandDao = RepoDatabase
                        .getInstance(getContext())
                        .getCommandDao();

                mCommandList = mCommandDao.findCommandsForRemote(mRemoteIDAsInt);


                mRemoteDao = RepoDatabase
                        .getInstance(getContext())
                        .getRemoteDao();

                List<Remote> mRemoteList = mRemoteDao.getRemote(mRemoteIDAsInt);
                mRemote = mRemoteList.get(0);
                createUI(mRemote);

            }
        });
    }


}






