package com.kyle.mycar.temp;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.kyle.mycar.db.DbOpenHelper;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class carDb {

    public static void init(Context context) {
        String s = "http://apicloud.mob.com/car/brand/query?key=1ecfd56b3a627";
        try {
            URL url = new URL(s);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            String s1 = streamToString(is);
            Gson gson = new Gson();
            CarInfo json = gson.fromJson(s1, CarInfo.class);
            List<CarInfo.ResultBean> result = json.result;

            Dao<CarBrand, Integer> dao = DbOpenHelper.getInstance(context).getDao(CarBrand.class);
            Dao<CarType, Integer> typeDao = DbOpenHelper.getInstance(context).getDao(CarType.class);


            for (CarInfo.ResultBean bean : result) {
                CarBrand carBrand = new CarBrand();
                carBrand.brand=bean.name;
                dao.create(carBrand);
                for (CarInfo.ResultBean.SonBean sonBean:bean.son) {
                    CarType carType = new CarType();
                    carType.carBrand=carBrand;
                    carType.car=sonBean.car;
                    carType.type=sonBean.type;
                    typeDao.create(carType);
                }
            }
            CarBrand carBrand = new CarBrand();
            carBrand.brand="其他";
            dao.create(carBrand);
            Logger.d("carDb------over");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            return null;
        }
    }

}
