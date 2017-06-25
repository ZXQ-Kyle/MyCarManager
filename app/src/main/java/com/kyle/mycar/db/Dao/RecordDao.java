package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.Record;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * Created by Zhang on 2017/5/15.
 */

public class RecordDao extends DaoUtils{


    private static RecordDao instance;

    private RecordDao(Context context, Class clazz) {
        super(context,clazz);
    }

    //修改表class！！！
    public static synchronized RecordDao getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            synchronized (RecordDao.class) {
                if (instance == null) {
                    instance=new RecordDao(context, Record.class);
                }
            }
        }
        return instance;
    }

    public Record queryNewestOne() {
        try {
            long count = mDao.countOf();
            return (Record) mDao.queryForId(count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long countOf() {
        try {
            return mDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**按时间降序排列，去除isDelete数据
     * @param off 直接使用pageCount
     * @param limit page_size
     * @return
     */
    public List<Record> queryOffestLimit(long off,long limit) {
        try {
            return mDao.queryBuilder().offset(off).limit(limit).orderBy("date",false).where().eq("isDelete",false).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
