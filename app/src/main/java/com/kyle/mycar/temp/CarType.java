package com.kyle.mycar.temp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "CarType")
public class CarType {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(foreign =true,foreignAutoRefresh = true,columnName = "CarBrand")
    public CarBrand carBrand;

    @DatabaseField
    public String car;

    @DatabaseField
    public String type;



}
