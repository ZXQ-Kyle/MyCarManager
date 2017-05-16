package com.kyle.mycar.db.Table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *mt和tag的映射表
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "MtMap")
public class MtMap {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Maintenance mt;

    @DatabaseField
    private String tag;

    @DatabaseField
    private boolean isDelete;

    @DatabaseField
    private String reserve;


    public MtMap() {
    }

    public MtMap(Maintenance mt, String tag) {
        this.mt = mt;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Maintenance getMt() {
        return mt;
    }

    public void setMt(Maintenance mt) {
        this.mt = mt;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}
