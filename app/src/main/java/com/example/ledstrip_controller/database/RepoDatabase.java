package com.example.ledstrip_controller.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = { Command.class, Remote.class },
        version = 1)
public abstract class RepoDatabase extends RoomDatabase {

    private static final String DB_NAME = "ledstripDatabase4.db";

    private static volatile RepoDatabase instance;

    public static synchronized RepoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static RepoDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                RepoDatabase.class,
                DB_NAME).build();
    }

    public abstract CommandDao getCommandDao();
    public abstract RemoteDao getRemoteDao();

}
