package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.Oil;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class OilDao extends DaoUtils{


    private static OilDao instance;

    private OilDao(Context context,Class clazz) {
        super(context,clazz);
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

    /**按时间降序排序后，查询最新一个数据
     *  并且isDelete=false
     * @param column
     * @param value
     * @return
     */
    public List<Oil> queryNewest(String column, Object value) {
        try {
            return mDao.queryBuilder()
                    .limit(1l)
                    .orderBy("date",false)
                    .where()
                    .eq("isDelete", false).and()
                    .eq(column, value)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 按时间降序排序后查询按两个value之间的数据
     * 并且isDelete=false
     * @param column
     * @param low high 包括low和high的值
     * @return
     */
    public List<Oil> queryBetween(String column, Object low,Object high) {
        try {
            return mDao.queryBuilder()
                    .orderBy("date",false)
                    .where()
                    .eq("isDelete", false).and()
                    .between(column,low,high)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
