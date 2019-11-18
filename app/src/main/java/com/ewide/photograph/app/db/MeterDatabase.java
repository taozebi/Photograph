package com.ewide.photograph.app.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ewide.photograph.app.db.dao.ProjectDao;
import com.ewide.photograph.app.project.model.entry.Project;

/**
 * author：Taozebi
 * date：2018/11/04 15:13
 * describe：数据库
 * 2019-11-17 version=1
 */

@Database(entities = {  Project.class}, version = 1, exportSchema = false)
public abstract class MeterDatabase extends RoomDatabase {

    private static final String DB_NAME = "photograph.db";
    private static volatile MeterDatabase instance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //TODO do something
            // Meterinfo 添加breachMoney 违约金字段
//            database.execSQL("ALTER TABLE Meterinfo ADD COLUMN breachMoney REAL NOT NULL DEFAULT 0.0");
        }
    };



    public static synchronized MeterDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static MeterDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                MeterDatabase.class,
                DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .build();
    }


    public abstract ProjectDao getProjectDao();


}
