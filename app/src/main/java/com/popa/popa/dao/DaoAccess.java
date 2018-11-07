package com.popa.popa.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.popa.popa.model.PostureData;
import com.popa.popa.model.User;
import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertNewUser(User user);

    @Insert
    void insertPostureData(PostureData postureData);

    //@Query("SELECT * FROM PostureData WHERE timestamp > :timestamp")
    //@Query("SELECT * FROM PostureData WHERE timestamp > datetime('now', 'now', '-7 day')")
//    @Query("SELECT * FROM PostureData WHERE timestamp >= datetime('now', 'now', :from) AND timestamp <= datetime('now', 'now', :to)")
//    List<PostureData> getPostureDataAfterTime(String from, String to);

    @Query("SELECT * FROM PostureData")
    List<PostureData> getPostureDataAfterTime();

    @Query("DELETE FROM PostureData")
    void deletePostureData();
}
