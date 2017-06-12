package com.kyle.mycar.db.Dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.MtTag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zhang on 2017/5/15.
 */

public abstract class DaoUtils<T> {

    public Dao mDao;

    protected DaoUtils(Context context, Class clazz) {
        DbOpenHelper helper = DbOpenHelper.getInstance(context.getApplicationContext());
        try {
            mDao = helper.getDao(clazz);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public long countOf() {
        try {
            return mDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 新增数据
     *
     * @param obj
     * @return The number of rows updated in the database,or -1
     */
    public int add(T obj) {
        try {
            return mDao.create(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除数据
     *
     * @param obj
     * @return The number of rows updated in the database.or -1
     */
    public int delete(T obj) {
        try {
            return mDao.delete(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * 更新数据
     * @param obj
     * @return The number of rows updated in the database.or -1
     */
    public int update(T obj) {
        try {
            return mDao.update(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 查询指定 Bean 所有数据
     *
     * @return
     */
    public List queryAll() {
        try {
            return mDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T queryForId(int id) {
        try {
            return (T) mDao.queryForId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param orderBy 排序列
     * @param asc     false表示降序，true表示升序
     * @return
     */
    public List<T> queryAllButIsDelete(String orderBy, boolean asc) {
        try {
            return mDao.queryBuilder().orderBy(orderBy, asc).where().eq("isDelete", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param column  查询条件
     * @param value   条件值
     * @param orderBy 排序列
     * @param asc     false表示降序，true表示升序
     * @return 对象bean
     */
    public List<T> queryButIsDelete(String column, Object value, String orderBy, boolean asc) {
        try {
            return mDao.queryBuilder().orderBy(orderBy, asc).where().eq("isDelete", false).and().eq(column, value)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param column  查询条件
     * @param value   条件值
     * @param orderBy 排序列
     * @param asc     false表示降序，true表示升序
     * @return 对象bean
     */
    public List<MtTag> query(String column, Object value, String orderBy, boolean asc) {
        try {
            return mDao.queryBuilder().orderBy(orderBy, asc).where().eq(column, value)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QueryBuilder queryBuilder() {
        return mDao.queryBuilder();
    }
}
