package com.kyle.mycar.Fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyle.mycar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {


    @BindView(R.id.iv_logo_about)
    ImageView ivLogoAbout;
    @BindView(R.id.tv_version_about)
    TextView tvVersionAbout;
    Unbinder unbinder;

    @Override
    public View initView() {
        noEventBus = true;
        return View.inflate(mActivity, R.layout.fragment_about, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.about, 2, 0, null);
        Glide.with(mActivity.getApplicationContext()).load(R.drawable.logo).into(ivLogoAbout);

        try {
            PackageInfo info = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            String versionName = info.versionName;
            tvVersionAbout.setText("v"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tvVersionAbout.setText("v0.1");
        }

    }

}
