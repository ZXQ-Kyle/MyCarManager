package com.kyle.mycar.db.Table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * Created by Zhang on 2017/5/15.
 */
@DatabaseTable(tableName = "CarBrand")
public class CarBrand {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String brand;

}
