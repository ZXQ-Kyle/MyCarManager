package com.kyle.mycar.Fragment;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.R;
import com.kyle.mycar.View.MyItemDecoration;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.OilDao;
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
    private int openRecordPosition = -1;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        return view;
    }


    @Override
    public void initData() {
        initToolbar(R.string.history, 1, R.menu.toolbar_main, this);
        srl.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new MyItemDecoration(mActivity));
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        mAdapter.setOnItemChildClickListener(this);
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(),0,MsgMainFragment.SET_ADAPTER));
        pageCount=1;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MsgMainFragment msg) {
        switch (msg.getFlag()) {
            case MsgMainFragment.SET_ADAPTER:
                if (mAdapter == null) {
                    mAdapter = new MultiAdapter(msg.getTag());
                }else {
                    mAdapter.setNewData(msg.getTag());
                }
                recyclerView.setAdapter(mAdapter);
                break;
            case MsgMainFragment.REFRESH:
                mAdapter.setNewData(msg.getTag());
                mAdapter.setEnableLoadMore(true);
                srl.setRefreshing(false);
                openRecordPosition = -1;
                break;
            case MsgMainFragment.UPDATE_AN_NEW_ONE_DATA:
                RecordDao dao = RecordDao.getInstance(mActivity);
                Record record = dao.queryNewestOne();
                if (record != null) {
                    mAdapter.addData(0, record);
                }
                recyclerView.smoothScrollToPosition(0);
                openRecordPosition++;
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
//                getData(0, MsgMainFragment.UPDATE_REFRESH);
                mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(),0,MsgMainFragment.UPDATE_REFRESH));
                pageCount=1;
                break;
            case MsgMainFragment.UPDATE_REFRESH:
                mAdapter.setNewData(msg.getTag());
                openRecordPosition = -1;
                break;

        }
    }

    //srl.setOnRefreshListener(this);
    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
//        getData(0, MsgMainFragment.REFRESH);
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(),0,MsgMainFragment.REFRESH));
        pageCount=1;
    }

    // mAdapter.setOnItemClickListener(this);
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (openRecordPosition != position) {
            openItem(position);
            if (openRecordPosition != -1) {
                openItem(openRecordPosition);
            }
            openRecordPosition = position;
        } else if (openRecordPosition == position) {
            openItem(position);
            openRecordPosition = -1;
        }
    }

    public void openItem(int position) {
        Record record = mAdapter.getData().get(position);
        record.isVisible = !record.isVisible;
        mAdapter.notifyItemChanged(position);
    }

    // mAdapter.setOnLoadMoreListener(this,recyclerView);
    @Override
    public void onLoadMoreRequested() {
//        getData(pageCount, MsgMainFragment.LOAD_MORE);
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(),pageCount,MsgMainFragment.LOAD_MORE));
        pageCount++;
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
                        mActivity.switchFrag(MainFragment.class, MaintenanceFragment.class, false, mt);

//                        MaintenanceFragment fragment = new MaintenanceFragment();
//                        fragment.mt = mt;
//                        mActivity.getSupportFragmentManager().beginTransaction().hide(this).add(R.id.fl_content,
//                                fragment,fragment.getClass().getSimpleName()).commit();
//                        mActivity.addToBackStack(fragment);
                        break;
                    case Record.FLAG_OIL:
                        Oil oil = record.getOil();
                        mActivity.switchFrag(MainFragment.class, OilFragment.class, false, oil);

//                        OilFragment fragment1 = new OilFragment();
//                        fragment1.oil=oil;
//                        mActivity.getSupportFragmentManager().beginTransaction().hide(this).add(R.id.fl_content,
//                                fragment1,fragment1.getClass().getSimpleName()).commit();
//                        mActivity.addToBackStack(fragment1);
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_oil:
                mActivity.switchFrag(MainFragment.class, OilFragment.class, false);
                break;
            case R.id.menu_main_expense:
                mActivity.switchFrag(MainFragment.class, MaintenanceFragment.class, false);
                break;
        }
        return true;
    }


    private static class getDataRun implements Runnable {
        private Context mContext;
        private long mOff;
        private int mWhat;
        /**
         * @param off  刷新第一页，为0，其余设置pageCount
         * @param what 获取数据后返回
         */
        public getDataRun(Context context, long off, int what) {
            mContext = context;
            mOff = off;
            mWhat = what;
        }
        @Override
        public void run() {
//            pageCount = off + 1;
            RecordDao dao = RecordDao.getInstance(mContext);
            long count = dao.countOf();
            long maxLine = count - mOff * PAGE_SIZE;
            if (maxLine <= 0) {
                //数据到底了，并且是上拉加载更多的情况,或者初次进入无数据
                if (mWhat == MsgMainFragment.LOAD_MORE) {
                    EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.LOAD_MORE_END));
                } else if (mWhat == MsgMainFragment.SET_ADAPTER) {
                    EventBus.getDefault().post(new MsgMainFragment(mWhat));
                }
            } else {
                List<Record> beanList = dao.queryOffestLimit(count - maxLine, PAGE_SIZE);
                EventBus.getDefault().post(new MsgMainFragment(mWhat, beanList));
            }
        }
    }
}
