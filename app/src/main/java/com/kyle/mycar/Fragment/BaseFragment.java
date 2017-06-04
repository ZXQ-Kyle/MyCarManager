package com.kyle.mycar.Fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MainActivity;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.R;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import org.greenrobot.eventbus.EventBus;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    public MainActivity mActivity;
    Unbinder unbinder;
    public Toolbar mToolbar;

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = initView();
        unbinder = ButterKnife.bind(this, view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        return view;
    }

    /**
     * 初始化Toolbar，必须放到initData中
     *  @param strID  标题
     * @param flag   1左边栏导航式样，2返回式样
     * @param menuId      0 不生成菜单
     * @param listener      监听
     */
    protected void initToolbar(int strID, int flag, int menuId, Toolbar.OnMenuItemClickListener listener) {
        if (flag == 1) {
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new MessageEvent(MyConstant.OPEN_DRAWER));
                }
            });
        } else {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.onBackPressed();
                }
            });
        }
        mToolbar.setTitle(strID);
        mToolbar.setBackgroundResource(R.drawable.toolbar);
        if (menuId !=0){
            mToolbar.inflateMenu(menuId);
        }
        mToolbar.setOnMenuItemClickListener(listener);
    }

//    protected void setStatubarColor(int color, int color2) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = mActivity.getWindow();
//            window.setNavigationBarColor(getResources().getColor(color));
//            window.setStatusBarColor(getResources().getColor(color2));
//        }
//    }


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
//        mActivity = getActivity();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, 300);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    // 初始化数据, 必须由子类实现
    public abstract void initData();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
