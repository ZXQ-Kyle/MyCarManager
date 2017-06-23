package com.kyle.mycar;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.kyle.mycar.Bean.UserInfo;
import com.mob.MobApplication;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 捕获全局异常
 * Created by Zhang on 2017/4/19.
 */

public class MyApplication extends MobApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(UserInfo.class);
        AVOSCloud.initialize(this,"YBNnB34DIKdwFJ0hNaaIhtE3-gzGzoHsz","y8Q0glNHlziIAKgwIr0Ys8Rd");

        CrashReport.initCrashReport(getApplicationContext(), "461eb24442", true);
//        CrashReport.initCrashReport(getApplicationContext(), "461eb24442", false);

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable throwable) {
//                //捕获到异常后的操作
//                Logger.e(throwable.toString());
//
//                System.exit(0);
//            }
//        });

    }
}
