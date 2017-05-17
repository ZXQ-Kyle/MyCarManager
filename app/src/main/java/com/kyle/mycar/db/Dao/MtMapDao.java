package com.kyle.mycar.db.Dao;

import android.content.Context;

import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtMap;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class MtMapDao extends DaoUtils{


    private static MtMapDao instance;

    private MtMapDao(Context context, Class clazz) {
        super(context,clazz);
    }


    public static synchronized MtMapDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (MtMapDao.class) {
                if (instance == null) {
                    instance=new MtMapDao(context, MtMap.class);
                }
            }
        }
        return instance;
    }



}
