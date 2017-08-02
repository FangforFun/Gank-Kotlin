package com.gkzxhn.gank_kotlin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.greendao.gen.DaoMaster;
import com.greendao.gen.RemindDao;

/**
 * Created by 方 on 2017/5/15.
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db,RemindDao.class);
    }
}
