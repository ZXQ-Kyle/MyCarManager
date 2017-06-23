package com.kyle.mycar.db.Table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    private String tags;

    public Maintenance() {

    }


    public Maintenance(long date, String money, String odometer,String tags) {
        this.date = date;
        this.money = money;
        this.odometer = odometer;
        this.tags=tags;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Maintenance{" + "id=" + id + ", date=" + date + ", money='" + money + '\'' + ", odometer='" +
                odometer + '\'' + ", isDelete=" + isDelete + ", tags=" +'}';
    }

}
