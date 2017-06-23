package com.kyle.mycar.db.Table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "Oil")
public class Oil {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private long date;

    @DatabaseField
    private String money;
    @DatabaseField
    private String price;
    @DatabaseField
    private String quantity;

    @DatabaseField
    private String odometer;

    @DatabaseField
    private String oilType;

    @DatabaseField
    private boolean isFull;

    @DatabaseField
    private boolean isForgetLast;

    @DatabaseField
    private boolean isDelete;

    @DatabaseField
    private String pricePerKm;
    @DatabaseField
    private String fuelC;

    @DatabaseField
    private String note;

    public Oil() {
    }

    public void update(long date, String money, String price, String quantity, String odometer, String oilType, boolean
            isFull, boolean isForgetLast, String pricePerKm, String fuelC){
        this.date = date;
        this.money = money;
        this.price = price;
        this.quantity = quantity;
        this.odometer = odometer;
        this.oilType = oilType;
        this.isFull = isFull;
        this.isForgetLast = isForgetLast;
        this.pricePerKm = pricePerKm;
        this.fuelC = fuelC;
    }

    public Oil(long date, String money, String price, String quantity, String odometer, String oilType, boolean
            isFull, boolean isForgetLast, String pricePerKm, String fuelC) {
        this.date = date;
        this.money = money;
        this.price = price;
        this.quantity = quantity;
        this.odometer = odometer;
        this.oilType = oilType;
        this.isFull = isFull;
        this.isForgetLast = isForgetLast;
        this.pricePerKm = pricePerKm;
        this.fuelC = fuelC;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isForgetLast() {
        return isForgetLast;
    }

    public void setForgetLast(boolean forgetLast) {
        isForgetLast = forgetLast;
    }


    public String getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(String pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public String getFuelC() {
        return fuelC;
    }

    public void setFuelC(String fuelC) {
        this.fuelC = fuelC;
    }

//    @Override
//    public String toString() {
//        return "Oil{" + "id=" + id + ", isDelete=" + isDelete + '}';
//    }


}
