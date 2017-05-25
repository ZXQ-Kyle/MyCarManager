package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.kyle.mycar.db.Table.Oil;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class RecordDao extends DaoUtils{


    private static RecordDao instance;

    private RecordDao(Context context, Class clazz) {
        super(context,clazz);
    }



    public static synchronized RecordDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (RecordDao.class) {
                if (instance == null) {
                    instance=new RecordDao(context, Oil.class);
                }
            }
        }
        return instance;
    }

}
