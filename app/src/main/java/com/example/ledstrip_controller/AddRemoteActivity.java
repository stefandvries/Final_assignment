package com.example.ledstrip_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ledstrip_controller.database.Command;
import com.example.ledstrip_controller.database.CommandDao;
import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import yuku.ambilwarna.AmbilWarnaDialog;


public class AddRemoteActivity extends AppCompatActivity {

    int mDefaultColor;
    Button mColorPickerButton;
    Button mDoneButton;
    Button mAddCommandButton;
    EditText mRemoteNameInput;
    EditText mRemoteBaseURLInput;
    TextView mNumberOfCommandsNumber;
    EditText mButtonNameInput;
    EditText mCommandURLInput;
    int NumberOfCommands = 0;
    Remote mRemote = null;
    List<Command> mCommandlist = new ArrayList<>();
    int mRemoteID;
    private RemoteDao mRemoteDao;
    private CommandDao mCommandDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remote);

        mAddCommandButton = findViewById(R.id.AddCommandButton);
        mRemoteNameInput = findViewById(R.id.RemoteNameInput);
        mRemoteBaseURLInput = findViewById(R.id.RemoteBaseURLInput);
        mNumberOfCommandsNumber = findViewById(R.id.NummerOfCommandsNumber);
        mButtonNameInput = findViewById(R.id.ButtonNameInput);
        mCommandURLInput = findViewById(R.id.ButtonURLInput);
        mDoneButton = findViewById(R.id.DoneButton);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRemote == null) {
                    Toast.makeText(getApplicationContext(), R.string.no_specified_remote, Toast.LENGTH_LONG).show();
                } else if (mCommandlist.size() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.no_specified_commands, Toast.LENGTH_LONG).show();
                } else if (mRemote != null && mCommandlist.size() != 0) {
                    // Save Remote
                    saveRemote(mRemote);

                    // Save all Commands
                    saveCommands();

                    // return to previous activity
                    returnActivity();
                }

            }
        });


        // Create OnClickListener
        mAddCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NumberOfCommands == 0) {
                    Random mRandom = new Random();
                    mRemoteID = mRandom.nextInt(100);
                    String mRemoteName = mRemoteNameInput.getText().toString();
                    String mRemoteBaseURL = mRemoteBaseURLInput.getText().toString();
                    mRemote = new Remote(mRemoteID, mRemoteName, mRemoteBaseURL, false);
                }


                String mButtonName = mButtonNameInput.getText().toString();
                String mCommandURL = mCommandURLInput.getText().toString();

                if (!mButtonName.equals("")) {
                    Command mCommand = new Command(mButtonName, mCommandURL, mRemoteID, mDefaultColor);
                    mCommandlist.add(mCommand);
                    NumberOfCommands++;
                }
                clearFields();

            }
        });



        // Create ColorPicker
        mDefaultColor = ContextCompat.getColor(AddRemoteActivity.this, R.color.colorPrimary);
        mColorPickerButton = findViewById(R.id.ColorPickerButton);
        mColorPickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openColorPicker();
            }

        });

    }

    private void saveCommands() {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {
                mCommandDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getCommandDao();

                for (int i = 0; i < mCommandlist.size(); i++) {

                    mCommandDao.insert(mCommandlist.get(i));

                }
            }
        });


    }

    private void saveRemote(final Remote remote) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {
                mRemoteDao = RepoDatabase
                        .getInstance(getApplicationContext())
                        .getRemoteDao();

                mRemoteDao.insert(remote);

            }
        });

    }

    private void clearFields() {
        mRemoteNameInput.setEnabled(false);
        mRemoteBaseURLInput.setEnabled(false);
        mButtonNameInput.setText("");
        mCommandURLInput.setText("");
        mNumberOfCommandsNumber.setText(String.valueOf(NumberOfCommands));

    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mColorPickerButton.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }


    private void returnActivity() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
