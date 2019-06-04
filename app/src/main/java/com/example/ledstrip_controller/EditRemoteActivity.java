package com.example.ledstrip_controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditRemoteActivity extends AppCompatActivity {

    EditText mRemoteNameEditText;
    EditText mRemoteIPEditText;
    Button mUpdateButton;

    RemoteDao mRemoteDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remote);

        mRemoteNameEditText = findViewById(R.id.remoteName);
        mRemoteIPEditText = findViewById(R.id.remoteIP);
        mUpdateButton = findViewById(R.id.updateButton);

        Intent intent = getIntent();
        final int mRemoteID = intent.getIntExtra("remoteID", 0);

        getRemote(mRemoteID);


        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRemote(mRemoteID);
            }
        });

    }

    private Remote getRemote(final int mRemoteID) {
        Remote mRemote = null;
        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getRemoteDao();

                List<Remote> mRemoteList = new ArrayList<>();
                mRemoteList = mRemoteDao.getRemote(mRemoteID);

                Remote mRemote = mRemoteList.get(0);

                buildUI(mRemote);
            }
        });
        return mRemote;

    }

    private void buildUI(Remote mRemote) {
        mRemoteNameEditText.setText(mRemote.mName);
        mRemoteIPEditText.setText(mRemote.mBaseLink);
    }

    private void updateRemote(final int mRemoteID) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getRemoteDao();

                String mRemoteName = String.valueOf(mRemoteNameEditText.getText());
                String mRemoteIP = String.valueOf(mRemoteIPEditText.getText());

                Remote mUpdatedButton = new Remote(mRemoteID, mRemoteName, mRemoteIP, true);
                mRemoteDao.update(mUpdatedButton);

                finish();
            }});
    }
}
