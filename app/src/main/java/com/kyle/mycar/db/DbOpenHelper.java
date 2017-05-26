package com.kyle.mycar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtTag;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.OilType;
import com.kyle.mycar.db.Table.Record;

import java.sql.SQLException;

/**
 * Created by Zhang on 2017/5/8.
 */

public class DbOpenHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "MyCar.db";

    private static DbOpenHelper instance;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,Maintenance.class);
            TableUtils.createTable(connectionSource,MtTag.class);
            TableUtils.createTable(connectionSource, OilType.class);
            TableUtils.createTable(connectionSource, Oil.class);
            TableUtils.createTable(connectionSource, Record.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //更新数据库
        try {
            TableUtils.dropTable(connectionSource,Oil.class,true);
            TableUtils.dropTable(connectionSource,OilType.class,true);
            TableUtils.createTable(connectionSource, Maintenance.class);
            TableUtils.createTable(connectionSource, MtTag.class);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例获取Helper
     *
     * @param context
     * @return
     */
    public static synchronized DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (DbOpenHelper.class) {
                if (instance == null) {
                    instance = new DbOpenHelper(context);
                }
            }
        }
        return instance;
    }

}
