package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Record;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter
        .OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private MultiAdapter mAdapter=new MultiAdapter(null);
    public static final int PAGE_SIZE=8;
    private long pageCount =1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        srl.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnLoadMoreListener(this,recyclerView);

        getData(pageCount, MsgMainFragment.SET_ADAPTER);
    }

    private void getData(final long off, final int what) {

        new Thread() {
            @Override
            public void run() {
                RecordDao dao = RecordDao.getInstance(mActivity);
                long count = dao.countOf();
                long maxLine = count - (off-1) * PAGE_SIZE;
                if (maxLine<=0){
                    //数据到底了，并且是上拉加载更多的情况,或者初次进入无数据
                    if (what==MsgMainFragment.LOAD_MORE){
                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.LOAD_MORE_END));
                    }else if (what==MsgMainFragment.SET_ADAPTER){
                        EventBus.getDefault().post(new MsgMainFragment(what));
                    }
                }else {
                    List<Record> beanList = dao.queryOffestLimit(count-maxLine, PAGE_SIZE);
                    EventBus.getDefault().post(new MsgMainFragment(what,beanList));
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MsgMainFragment msg) {
        switch (msg.getFlag()) {
            case MsgMainFragment.SET_ADAPTER:
                if (mAdapter==null){
                    mAdapter=new MultiAdapter(null);
                }
                mAdapter.setNewData(msg.getTag());
                recyclerView.setAdapter(mAdapter);
                break;
            case MsgMainFragment.REFRESH:
                mAdapter.setNewData(msg.getTag());
                mAdapter.setEnableLoadMore(true);
                srl.setRefreshing(false);
                break;
            case MsgMainFragment.UPDATE_AN_NEW_ONE_DATA:
                RecordDao dao = RecordDao.getInstance(mActivity);
                Record record = dao.queryNewestOne();
                if (record!=null){
                    mAdapter.addData(0,record);
                }
                recyclerView.smoothScrollToPosition(0);
                break;
            case MsgMainFragment.LOAD_MORE:
                mAdapter.loadMoreComplete();
                mAdapter.addData(msg.getTag());
                Logger.d("MsgMainFragment.MsgMainFragment.LOAD_MORE");
                break;
            case MsgMainFragment.LOAD_MORE_END:
                mAdapter.loadMoreEnd();
                Logger.d("MsgMainFragment.LOAD_MORE_END");
                break;
        }
    }

    //srl.setOnRefreshListener(this);
    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        getData(0, MsgMainFragment.REFRESH);
        pageCount=1;
    }

    // mAdapter.setOnItemClickListener(this);
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
    // mAdapter.setOnLoadMoreListener(this,recyclerView);
    @Override
    public void onLoadMoreRequested() {
        pageCount++;
        getData(pageCount, MsgMainFragment.LOAD_MORE);
    }
}
