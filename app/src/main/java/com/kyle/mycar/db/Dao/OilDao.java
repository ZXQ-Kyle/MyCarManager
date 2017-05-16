package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.Oil;
import java.sql.SQLException;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class OilDao extends DaoUtils{


    private static OilDao instance;

    private OilDao(Context context,Class clazz) {
        super(context,clazz);
        Log.i("---", "class class: "+mDao.toString());
    }



    public static synchronized OilDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (OilDao.class) {
                if (instance == null) {
                    instance=new OilDao(context, Oil.class);
                }
            }
        }
        return instance;
    }

}
