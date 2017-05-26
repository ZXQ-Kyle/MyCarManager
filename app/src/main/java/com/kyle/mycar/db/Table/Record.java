package com.kyle.mycar.db.Table;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "Record")
public class Record implements MultiItemEntity {
    public static final String COLUMN_OIL_ID="oil_id";
    public static final String COLUMN_MT_ID="mt_id";
    public static final int FLAG_OIL=0;
    public static final int FLAG_MT=1;


    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "oil_id",foreign = true,foreignAutoRefresh = true)
    private Oil oil;

    @DatabaseField(columnName = "mt_id",foreign = true,foreignAutoRefresh = true)
    private Maintenance mt;

    @DatabaseField
    private boolean isDelete;

    @DatabaseField
    private long date;

    @DatabaseField
    private int flag;

    public boolean isVisible;

    public Record() {
    }

    public Record(Oil oil, long date) {
        this(oil,null,false,date,FLAG_OIL);
    }

    public Record(Maintenance mt, long date) {
        this(null,mt,false,date,FLAG_MT);
    }

    public Record(Oil oil, Maintenance mt, boolean isDelete, long date, int flag) {
        this.oil = oil;
        this.mt = mt;
        this.isDelete = isDelete;
        this.date = date;
        this.flag = flag;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Oil getOil() {
        return oil;
    }

    public void setOil(Oil oil) {
        this.oil = oil;
    }

    public Maintenance getMt() {
        return mt;
    }

    public void setMt(Maintenance mt) {
        this.mt = mt;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

//    @Override
//    public String toString() {
//        return "Record{" + "id=" + id + ", oil=" + oil + ", mt=" + mt + ", isDelete=" + isDelete + ", date='" + date
//                + '\'' + ", flag=" + flag + '}';
//    }


    @Override
    public String toString() {
        return "Record{" + "id=" + id + ", isDelete=" + isDelete + '}';
    }

    /**
     * @return flag
     */
    @Override
    public int getItemType() {
        return flag;
    }
}
