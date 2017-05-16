package com.kyle.mycar.db.Table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "MtTag")
public class MtTag {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String tag;

    @DatabaseField
    private boolean isDelete;

    @DatabaseField
    private String reserve;


    public MtTag() {
    }

    public MtTag(String tag) {
        this.tag = tag;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    @Override
    public String toString() {
        return "MtTag{" + "id=" + id + ", tag='" + tag + '\'' + ", isDelete=" + isDelete + '}';
    }
}
