package com.kyle.mycar.Bean;

import com.kyle.mycar.db.Table.MtMap;

import java.util.List;

/**
 * Created by Zhang on 2017/5/17.
 */

public class ItemTagBean {
    private String date;
    private String money;
    private List<MtMap> tags;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<MtMap> getTags() {
        return tags;
    }

    public void setTags(List<MtMap> tags) {
        this.tags = tags;
    }
}
