package com.example.ledstrip_controller.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Remote.class,
        parentColumns = "id",
        childColumns = "remoteId",
        onDelete = CASCADE))

public class Command {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public final String name;
    public final String url;
    public final int remoteId;
    public final int color;

    public Command( String name, String url,
                final int remoteId, int color) {
        this.name = name;
        this.url = url;
        this.remoteId = remoteId;
        this.color = color;
    }
}
