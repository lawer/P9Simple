package com.company.p9simple.db;

import com.company.p9simple.model.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public abstract class AppDao {
    @Insert
    public abstract void insertUser(User user);

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    public abstract LiveData<User> authenticate(String username, String password);

    @Query("SELECT * FROM User WHERE username = :username")
    public abstract LiveData<User> getUser(String username);
}
