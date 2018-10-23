package com.popa.popa.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import com.popa.popa.model.User;

@Dao
public interface DaoAccess {
    @Insert
    void insertNewUser(User user);
}
