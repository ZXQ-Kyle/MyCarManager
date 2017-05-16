package com.kyle.mycar.db.Table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "OilType")
public class OilType {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String oilType;

    @DatabaseField
    private boolean isDelete;

    @DatabaseField
    private String reserve;

    public OilType() {
    }

    public OilType(String oilType) {
        this.oilType = oilType;
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

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    @Override
    public String toString() {
        return "OilType{" + "id=" + id + ", oilType='" + oilType + '\'' + '}';
    }
}
