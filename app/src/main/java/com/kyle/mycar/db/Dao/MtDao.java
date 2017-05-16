package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.OilType;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class MtDao extends DaoUtils{


    private static MtDao instance;

    private MtDao(Context context, Class clazz) {
        super(context,clazz);
    }


    public static synchronized MtDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (MtDao.class) {
                if (instance == null) {
                    instance=new MtDao(context, Maintenance.class);
                }
            }
        }
        return instance;
    }

}
