package com.popa.popa.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.popa.popa.model.PostureData;
import com.popa.popa.model.User;

@Dao
public interface DaoAccess {
    @Insert
    void insertNewUser(User user);
    @Insert
    void insertPostureData(PostureData postureData);
    @Query("SELECT * FROM PostureData WHERE timestamp > :timestamp")
    PostureData getPostureDataAfterTime (String timestamp);
}
