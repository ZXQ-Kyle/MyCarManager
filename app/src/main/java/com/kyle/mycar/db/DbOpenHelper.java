package com.kyle.mycar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by Zhang on 2017/5/8.
 */

public class DbOpenHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "MyCar.db";
    public static final int DB_VERSION = 1;


    private static DbOpenHelper instance;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//        try {
//            TableUtils.createTable(connectionSource,Maintenance.class);
//            TableUtils.createTable(connectionSource,MtTag.class);
//            TableUtils.createTable(connectionSource, OilType.class);
//            TableUtils.createTable(connectionSource, Oil.class);
//            TableUtils.createTable(connectionSource, Record.class);
//
//            TableUtils.createTable(connectionSource, CarBrand.class);
//            TableUtils.createTable(connectionSource, CarType.class);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //更新数据库
//            if (oldVersion<2){
//                try {
//                    Dao<Oil, Integer> dao = getDao(Oil.class);
//                    dao.executeRaw("ALTER TABLE `Oil` ADD COLUMN note TEXT DEFAULT '';");
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }

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
