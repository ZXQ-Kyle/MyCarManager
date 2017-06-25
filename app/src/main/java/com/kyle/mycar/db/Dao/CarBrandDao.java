package com.kyle.mycar.db.Dao;

import android.content.Context;

import com.kyle.mycar.db.Table.CarBrand;
import com.kyle.mycar.db.Table.Maintenance;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class CarBrandDao extends DaoUtils{


    private static CarBrandDao instance;

    private CarBrandDao(Context context, Class clazz) {
        super(context,clazz);
    }


    public static synchronized CarBrandDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (CarBrandDao.class) {
                if (instance == null) {
                    instance=new CarBrandDao(context, CarBrand.class);
                }
            }
        }
        return instance;
    }

}
