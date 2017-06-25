package com.kyle.mycar.db.Dao;

import android.content.Context;

import com.kyle.mycar.db.Table.CarType;
import com.kyle.mycar.db.Table.Maintenance;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class CarTypeDao extends DaoUtils{


    private static CarTypeDao instance;

    private CarTypeDao(Context context, Class clazz) {
        super(context,clazz);
    }


    public static synchronized CarTypeDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (CarTypeDao.class) {
                if (instance == null) {
                    instance=new CarTypeDao(context, CarType.class);
                }
            }
        }
        return instance;
    }

}
