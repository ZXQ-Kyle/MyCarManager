package com.kyle.mycar.Bean;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by Zhang on 2017/6/21.
 */
@AVClassName("UserInfo")
public class UserInfo extends AVObject {

    public UserInfo() {
        super();
    }

    public UserInfo(Parcel in) {
        super(in);
    }

    public String getPsw() {
        return getString("psw");
    }

    public void setPsw(String value) {
        put("psw", value);
    }
    public String getPhone() {
        return getString("phone");
    }

    public void setPhone(String value) {
        put("phone", value);
    }

    public static final Creator CREATOR = AVObjectCreator.instance;
}