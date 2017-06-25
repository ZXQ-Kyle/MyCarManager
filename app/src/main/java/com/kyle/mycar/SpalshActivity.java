package com.kyle.mycar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.db.DbOpenHelper;
import com.orhanobut.logger.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.wangyuwei.particleview.ParticleView;

public class SpalshActivity extends AppCompatActivity implements ParticleView.ParticleAnimListener, Runnable {

    @BindView(R.id.view)
    ParticleView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_spalsh);
        ButterKnife.bind(this);

        boolean b = SpUtils.getboolean(this, MyConstant.COPY_DB_SUCESS);
        if (!b){
            checkPermi();
        }else {
            view.setOnParticleAnimListener(this);
            view.startAnim();
        }


    }

    private void checkPermi() {
        if (Build.VERSION.SDK_INT >= 23) {
            int writeSdcard = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();

            if (writeSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }
        try {
            initDb();
            Snackbar.make(getWindow().getDecorView(),"复制初始数据成功",Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(getWindow().getDecorView(),"复制初始数据失败",Snackbar.LENGTH_LONG).show();
        }
        view.setOnParticleAnimListener(this);
        view.startAnim();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {

        checkPermi();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initDb() throws IOException {

        InputStream myInput  =  getAssets().open(DbOpenHelper.DB_NAME);

        String  outFileName  =  "/data/data/com.kyle.mycar/databases/"  +  DbOpenHelper.DB_NAME;

        OutputStream myOutput  =  new FileOutputStream(outFileName);

        byte[]  buffer  =  new  byte[1024];
        int  length;
        while  ((length  =  myInput.read(buffer))  >  0)  {
            myOutput.write(buffer,  0,  length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

        SpUtils.putboolean(this,MyConstant.COPY_DB_SUCESS,true);
    }

    @Override
    public void onAnimationEnd() {
        String string = SpUtils.getString(this, MyConstant.USER_ID);
        if (TextUtils.isEmpty(string)) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        Handler handler=new Handler();
        handler.postDelayed(this, 500);
    }


    @Override
    public void run() {
        finish();
    }
}
