package com.example.ledstrip_controller.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Remote {
    @PrimaryKey public final int id;
    public final String mBaseLink;
    public final String mName;
    public final boolean mIsFavorite;

    public Remote(int id, String mName, String mBaseLink, boolean mIsFavorite) {
        this.id = id;
        this.mBaseLink = mBaseLink;
        this.mName = mName;
        this.mIsFavorite = mIsFavorite;
    }

}


