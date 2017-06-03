package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyle.mycar.Bean.MsgChart;
import com.kyle.mycar.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.setting_head_tv)
    TextView settingHeadTv;
    @BindView(R.id.recycler_view_setting)
    RecyclerView recyclerView;
    Unbinder unbinder;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_setting, null);
    }

    @Override
    public void initData() {
//        initToolbar(R.string.setting, 2,0,null);
        mToolbar.setVisibility(View.GONE);
        mActivity.initHeadImage(head);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        String[] strings = getResources().getStringArray(R.array.settings);
        QuickAdapter adapter = new QuickAdapter(Arrays.asList(strings));
        recyclerView.setAdapter(adapter);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);

        adapter.isFirstOnly(false);
        adapter.setOnItemClickListener(this);




    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void no(MsgChart msgChart) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    private static class QuickAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

        public QuickAdapter(List list) {
            super(R.layout.item_recycle_setting,list);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.it_tv_setting,item)
                    .setImageResource(R.id.it_iv_setting,R.drawable.ic_menu_camera);
        }
    }

}
