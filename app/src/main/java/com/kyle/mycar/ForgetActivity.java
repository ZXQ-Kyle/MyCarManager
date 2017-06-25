package com.kyle.mycar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.kyle.mycar.Bean.UserInfo;
import com.kyle.mycar.Fragment.ForgetPsw2Fragment;
import com.kyle.mycar.Fragment.ForgetPswFragment;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetActivity extends AppCompatActivity implements Handler.Callback{

    public Handler handler=new Handler(this);
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(500));
            getWindow().setExitTransition(new Explode().setDuration(500));
        }
        registerSDK();
        ForgetPswFragment fragment = new ForgetPswFragment();
        fragment.handler=handler;
//        fragment.eventHandler=eventHandler;
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,fragment,"f1").commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private void registerSDK() {

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public boolean handleMessage(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;

        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                String phone = (String) ((HashMap) data).get("phone");
                Toast.makeText(this, "验证码正确", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,ForgetPsw2Fragment.newInstance(phone),"f2").commit();

            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                //获取验证码成功

            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                //返回支持发送验证码的国家列表
            } else if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
                //提交用户成功
            }
        } else {
            Throwable throwable = (Throwable) data;
            throwable.printStackTrace();
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(throwable.getMessage());
            String des = object.getString("detail");//错误描述
            int status = object.getInteger("status");//错误代码
            if (status > 0 && !TextUtils.isEmpty(des)) {
                Toast.makeText(this, des, Toast.LENGTH_LONG).show();
                return true;
            }
        }

        return false;
    }
}
