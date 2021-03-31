package com.lishuaihua.data_module.room;

import android.app.Application;

import androidx.room.Room;

import com.lishuaihua.data_module.model.User;

import java.util.List;

import static com.lishuaihua.data_module.room.RoomDataUpdate.migration_1_2;

public class RoomDataManager {
    private static RoomDataManager roomDataManager;
    private final static String dbName = "room_db";
    private AppDatabase db;

    /**
     * 初始化数据库
     *
     * @param application
     */
    public void init(Application application) {
        if (RoomDatabaseVersion.oldVersion == RoomDatabaseVersion.newVersion) {
            db = Room.databaseBuilder(application, AppDatabase.class, dbName).build();
        } else {
            int i = RoomDatabaseVersion.newVersion - RoomDatabaseVersion.oldVersion;
            if (i == 1) {
                db = Room.databaseBuilder(application,
                        AppDatabase.class, dbName)
                        .addMigrations(migration_1_2)
                        .build();
            }
        }
    }

    public static synchronized RoomDataManager getInstance() {
        if (roomDataManager == null) {
            roomDataManager = new RoomDataManager();
        }
        return roomDataManager;
    }


    /**
     * 新增.修改
     *
     * @param user
     */
    public void insertUser(User user) {
        db.userDao().insertUsers(user);
    }

    /**
     * 删除
     *
     * @param user
     */
    public void deleteUser(User user) {
        db.userDao().delete(user);
    }

    /**
     * 查询
     *
     * @return
     */
    public User queryUser() {
        User user = null;
        List<User> userList = db.userDao().getAll();
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
        }
        return user;
    }


}
