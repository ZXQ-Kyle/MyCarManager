package com.kyle.mycar.Fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    public FragmentActivity mActivity;
    Unbinder unbinder;
    public Toolbar mToolbar;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = initView();
        unbinder = ButterKnife.bind(this, view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        //调用Fragment的onCreateOptionsMenu()
//        setHasOptionsMenu(true);
        //设置点击开启左边栏
        return view;
    }

    /**初始化Toolbar
     * @param drawable   导航图标
     * @param strID     标题
     * @param color     颜色 浅
     * @param color2    颜色 深
     * @param flag      1左边栏导航式样，2返回式样
     */
    protected void initToolbar(int drawable,int strID,int color, int color2,int flag) {
        if (flag==1){
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new MessageEvent(MyConstant.OPEN_DRAWER));
                }
            });
        }else {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.onBackPressed();
                }
            });
        }
        mToolbar.setNavigationIcon(drawable);
        mToolbar.setTitle(strID);
        mToolbar.setBackgroundColor(getResources().getColor(color));
        setStatubarColor(color,color2);
    }

    protected void setStatubarColor(int color,int color2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.setNavigationBarColor(getResources().getColor(color));
            window.setStatusBarColor(getResources().getColor(color2));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    public abstract View initView();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    // 初始化数据, 必须由子类实现
    public abstract void initData();
}
