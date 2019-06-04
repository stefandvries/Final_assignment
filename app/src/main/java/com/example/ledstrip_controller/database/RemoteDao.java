package com.example.ledstrip_controller.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RemoteDao {

    @Query("SELECT * FROM remote")
    public LiveData<List<Remote>> getAllRemotes();


    @Query("DELETE FROM remote")
    public void nukeTable();

    @Insert
    void insert(Remote... remote);

    @Update
    void update(Remote... remote);

    @Delete
    void delete(Remote... remote);

    @Query("SELECT * FROM remote WHERE id=:remoteID")
    List<Remote> getRemote(final int remoteID);

    @Query("SELECT * FROM remote WHERE mIsFavorite='1'")
    List<Remote> getFavorite();



}