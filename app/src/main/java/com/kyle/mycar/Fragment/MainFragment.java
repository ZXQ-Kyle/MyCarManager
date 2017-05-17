package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyle.mycar.Bean.ItemTagBean;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.MtMapDao;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtMap;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class MainFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    PullLoadMoreRecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        ArrayList<ItemTagBean> tagBeens = new ArrayList<>();
        MtDao mtDao = MtDao.getInstance(mActivity);
        List<Maintenance> mt = mtDao.queryAllButIsDelete("date", true);
        MtMapDao mapDao = MtMapDao.getInstance(mActivity);
        for (Maintenance m : mt) {
            ItemTagBean bean = new ItemTagBean();
            bean.setDate(MyDateUtils.longToStr(m.getDate()));
            bean.setMoney(m.getMoney());
            List<MtMap> list = mapDao.queryButIsDelete("mt_id", m.getId(), "id", true);
            bean.setTags(list);
        }
        recyclerView.setLinearLayout();
        recyclerView.setAdapter(new RecyclerViewAdapter(tagBeens));
        //显示下拉刷新
//        recyclerView.setRefreshing(true);
        //调用下拉刷新和加载更多

//        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//
//            @Override
//            public void onLoadMore() {
//
//            }
//        });

        //快速Top
//        recyclerView.scrollToTop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
