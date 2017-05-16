package com.kyle.mycar.db.Table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "Maintenance")
public class Maintenance {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private long date;
    @DatabaseField
    private String money;
    @DatabaseField
    private String odometer;
    @DatabaseField
    private boolean isDelete;

    @DatabaseField
    private String reserve1;
    @DatabaseField
    private String reserve2;

    public Maintenance() {

    }

    public Maintenance(long date, String money, String odometer) {
        this.date = date;
        this.money = money;
        this.odometer = odometer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    @Override
    public String toString() {
        return "Maintenance{" + "id=" + id + ", date=" + date + ", money='" + money + '\'' + ", odometer='" +
                odometer + '\'' + ", isDelete=" + isDelete + ", tags=" +'}';
    }
}
