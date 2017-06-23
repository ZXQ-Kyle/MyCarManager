package com.kyle.mycar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.orhanobut.logger.Logger;

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

        view.startAnim();
        view.setOnParticleAnimListener(this);

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
