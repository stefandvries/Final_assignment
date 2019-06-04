package com.example.ledstrip_controller.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class DatabaseRepository {


    private RepoDatabase mAppDatabase;

    private RemoteDao mRemoteDao;
    private CommandDao mCommandDao;

    private LiveData<List<Remote>> mRemotes;

    private Executor mExecutor = Executors.newSingleThreadExecutor();


    public DatabaseRepository (Context context) {

        mAppDatabase = RepoDatabase.getInstance(context);

        mRemoteDao = mAppDatabase.getRemoteDao();
        mCommandDao = mAppDatabase.getCommandDao();

        mRemotes = mRemoteDao.getAllRemotes();

    }


    public LiveData<List<Remote>> getAllRemotes() {

        return mRemotes;

    }


    public void insert(final Remote remote) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao.insert(remote);

            }

        });

    }


    public void update(final Remote remote) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao.update(remote);

            }

        });

    }


    public void delete(final Remote remote) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mRemoteDao.delete(remote);

            }

        });

    }

}

