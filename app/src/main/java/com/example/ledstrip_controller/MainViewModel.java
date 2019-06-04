package com.example.ledstrip_controller;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.ledstrip_controller.database.DatabaseRepository;
import com.example.ledstrip_controller.database.Remote;

import java.util.List;


public class MainViewModel extends AndroidViewModel {


    private DatabaseRepository mRepository;

    private LiveData<List<Remote>> mRemotes;


    public MainViewModel(@NonNull Application application) {

        super(application);

        mRepository = new DatabaseRepository(application.getApplicationContext());

        mRemotes = mRepository.getAllRemotes();

    }


    public LiveData<List<Remote>> getReminders() {

        return mRemotes;

    }


    public void insert(Remote game) {

        mRepository.insert(game);

    }


    public void update(Remote game) {

        mRepository.update(game);

    }


    public void delete(Remote game) {

        mRepository.delete(game);

    }

}
