package com.popa.popa.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.popa.popa.dao.DaoAccess;
import com.popa.popa.model.PostureData;
import com.popa.popa.model.User;

@Database(entities = {User.class, PostureData.class}, version = 1, exportSchema = false)
public abstract class popaDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
