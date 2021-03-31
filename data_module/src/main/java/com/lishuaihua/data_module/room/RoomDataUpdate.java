package com.lishuaihua.data_module.room;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class RoomDataUpdate {
    static final Migration migration_1_2 = new Migration(RoomDatabaseVersion.oldVersion, RoomDatabaseVersion.newVersion) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };

}
