package com.kyle.mycar.Fragment;


import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.MainActivity;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Dao.OilDao;
import com.kyle.mycar.db.Dao.OilTypeDao;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.Record;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter
        .OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener,
        Toolbar.OnMenuItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private MultiAdapter mAdapter = new MultiAdapter(null);
    public static final int PAGE_SIZE = 8;
    private long pageCount = 0;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        return view;
    }


    @Override
    public void initData() {
        mToolbar.inflateMenu(R.menu.toolbar_main);
        initToolbar(R.string.history, R.color.colorPrimary, R.color.colorPrimaryDark, 1);
        mToolbar.setOnMenuItemClickListener(this);

        srl.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        mAdapter.setOnItemChildClickListener(this);
        getData(0, MsgMainFragment.SET_ADAPTER);

        new Thread() {
            @Override
            public void run() {
                List list = MtDao.getInstance(mActivity).queryAll();
                List list1 = MtTagDao.getInstance(mActivity).queryAll();
                List list2 = OilDao.getInstance(mActivity).queryAll();
                List list3 = OilTypeDao.getInstance(mActivity).queryAll();
                List list4 = RecordDao.getInstance(mActivity).queryAll();
                Logger.d(list);
                Logger.d(list1);
                Logger.d(list2);
                Logger.d(list3);
                Logger.d(list4);
            }
        }.start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            setStatubarColor(R.color.colorPrimary, R.color.colorPrimaryDark);
        }
    }


    /**
     * @param off  刷新第一页，为0，其余设置pageCount
     * @param what 获取数据后返回
     */
    private void getData(final long off, final int what) {
        pageCount = off + 1;
        new Thread() {
            @Override
            public void run() {
                RecordDao dao = RecordDao.getInstance(mActivity);
                long count = dao.countOf();
                long maxLine = count - off * PAGE_SIZE;
                if (maxLine <= 0) {
                    //数据到底了，并且是上拉加载更多的情况,或者初次进入无数据
                    if (what == MsgMainFragment.LOAD_MORE) {
                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.LOAD_MORE_END));
                    } else if (what == MsgMainFragment.SET_ADAPTER) {
                        EventBus.getDefault().post(new MsgMainFragment(what));
                    }
                } else {
                    List<Record> beanList = dao.queryOffestLimit(count - maxLine, PAGE_SIZE);
                    Logger.d(beanList);
                    EventBus.getDefault().post(new MsgMainFragment(what, beanList));
                }
            }
        }.start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MsgMainFragment msg) {
        switch (msg.getFlag()) {
            case MsgMainFragment.SET_ADAPTER:
                if (mAdapter == null) {
                    mAdapter = new MultiAdapter(null);
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
                if (record != null) {
                    mAdapter.addData(0, record);
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
            case MsgMainFragment.UPDATE_AN_OLD_DATA:
                getData(0, MsgMainFragment.UPDATE_REFRESH);
                break;
            case MsgMainFragment.UPDATE_REFRESH:
                mAdapter.setNewData(msg.getTag());
                break;

        }
    }

    //srl.setOnRefreshListener(this);
    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        getData(0, MsgMainFragment.REFRESH);
    }

    // mAdapter.setOnItemClickListener(this);
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Record record = mAdapter.getData().get(position);
        record.isVisible = !record.isVisible;
        mAdapter.notifyItemChanged(position);
    }

    // mAdapter.setOnLoadMoreListener(this,recyclerView);
    @Override
    public void onLoadMoreRequested() {
        getData(pageCount, MsgMainFragment.LOAD_MORE);
    }

    //mAdapter.setOnItemChildClickListener(this);
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        RecordDao dao = RecordDao.getInstance(mActivity);
        Record record = mAdapter.getData().get(position);
        switch (view.getId()) {
            //删除单条记录
            case R.id.it_iv_delete:
                switch (record.getItemType()) {
                    case Record.FLAG_MT:
                        Maintenance mt = record.getMt();
                        mt.setDelete(true);
                        MtDao.getInstance(mActivity).update(mt);
                        break;
                    case Record.FLAG_OIL:
                        Oil oil = record.getOil();
                        oil.setDelete(true);
                        OilDao.getInstance(mActivity).update(oil);
                        break;
                }
                record.setDelete(true);
                dao.update(record);
                mAdapter.getData().remove(position);
                if (mAdapter.getData().size() == 0) {
                    mAdapter.setNewData(null);
                } else {
                    mAdapter.notifyItemRemoved(position);
                }
                Snackbar.make(getView(), R.string.delete_sucess, Snackbar.LENGTH_LONG).show();
                break;
            //修改数据
            case R.id.it_iv_update:
                switch (record.getItemType()) {
                    case Record.FLAG_MT:
                        Maintenance mt = record.getMt();
                        MaintenanceFragment fragment = new MaintenanceFragment();
                        fragment.mt = mt;
                        mActivity.getSupportFragmentManager().beginTransaction().hide(this).add(R.id.fl_content,
                                fragment,fragment.getClass().getSimpleName()).commit();
                        mActivity.addToBackStack(fragment);
                        break;
                    case Record.FLAG_OIL:
                        Oil oil = record.getOil();
                        OilFragment fragment1 = new OilFragment();
                        fragment1.oil=oil;
                        mActivity.getSupportFragmentManager().beginTransaction().hide(this).add(R.id.fl_content,
                                fragment1,fragment1.getClass().getSimpleName()).commit();
                        mActivity.addToBackStack(fragment1);
                        break;
                }
                mActivity.fabMenu.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).show();
        return false;
    }
}
