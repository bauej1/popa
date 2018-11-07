package com.popa.popa.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.popa.popa.dao.DaoAccess;
import com.popa.popa.model.DateConverter;
import com.popa.popa.model.PostureData;
import com.popa.popa.model.User;

@Database(entities = {User.class, PostureData.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class popaDatabase extends RoomDatabase {

    private static popaDatabase instance;
    private static final String DATABASE_NAME = "popaDb";
    public abstract DaoAccess daoAccess();

    public static popaDatabase getDatabase(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), popaDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
