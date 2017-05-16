package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.kyle.mycar.db.Table.OilType;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class OilTypeDao extends DaoUtils{


    private static OilTypeDao instance;

    private OilTypeDao(Context context, Class clazz) {
        super(context,clazz);

        Log.i("---", "class OilTypeDao: "+mDao.toString());
    }


    public static synchronized OilTypeDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (OilTypeDao.class) {
                if (instance == null) {
                    instance=new OilTypeDao(context, OilType.class);
                }
            }
        }
        return instance;
    }

}
