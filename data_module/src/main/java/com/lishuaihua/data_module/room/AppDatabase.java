package com.lishuaihua.data_module.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.lishuaihua.data_module.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}


