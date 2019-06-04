package com.example.ledstrip_controller.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface CommandDao {

    @Insert
    void insert(Command command);

    @Update
    void update(Command... command);

    @Delete
    void delete(Command... command);
    @Query("SELECT * FROM command")
    List<Command> getAllCommands();

    @Query("SELECT * FROM command WHERE remoteId=:remoteID")
    List<Command> findCommandsForRemote(final int remoteID);
}
