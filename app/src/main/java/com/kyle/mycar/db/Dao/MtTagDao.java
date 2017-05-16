package com.kyle.mycar.db.Dao;

import android.content.Context;

import com.kyle.mycar.db.Table.MtTag;
import com.kyle.mycar.db.Table.OilType;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class MtTagDao extends DaoUtils{


    private static MtTagDao instance;

    private MtTagDao(Context context, Class clazz) {
        super(context,clazz);
    }


    public static synchronized MtTagDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (MtTagDao.class) {
                if (instance == null) {
                    instance=new MtTagDao(context, MtTag.class);
                }
            }
        }
        return instance;
    }

}
