package com.popa.popa.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.popa.popa.model.PostureData;
import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertPostureData(PostureData postureData);

    @Query("SELECT * FROM PostureData")
    List<PostureData> getPostureDataAfterTime();

    @Query("SELECT COUNT(*) FROM PostureData")
    int countPostureData();

    @Query("DELETE FROM PostureData")
    void deletePostureData();
}
