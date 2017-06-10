package com.kyle.mycar.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
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
    public static final int PAGE_SIZE = 10;
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
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        mAdapter.setOnItemChildClickListener(this);
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(), 0, MsgMainFragment
                .SET_ADAPTER));
        pageCount = 1;
        recyclerView.addItemDecoration(new MyItemDecoration(mActivity));

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MsgMainFragment msg) {
        switch (msg.getFlag()) {
            case MsgMainFragment.SET_ADAPTER:
                if (mAdapter == null) {
                    mAdapter = new MultiAdapter(msg.getRecordList());
                } else {
                    mAdapter.setNewData(msg.getRecordList());
                }
                recyclerView.setAdapter(mAdapter);
                break;
            case MsgMainFragment.REFRESH:
                mAdapter.setNewData(msg.getRecordList());
                mAdapter.setEnableLoadMore(true);
                srl.setRefreshing(false);
                openRecordPosition = -1;
                break;
            case MsgMainFragment.UPDATE_AN_NEW_ONE_DATA:
                Record msgRecord = msg.getRecord();
                if (msgRecord != null) {
                    mAdapter.addData(0, msgRecord);
                }
                recyclerView.smoothScrollToPosition(0);
                if (openRecordPosition != -1) openRecordPosition++;
                break;
            case MsgMainFragment.LOAD_MORE:
                mAdapter.loadMoreComplete();
                mAdapter.addData(msg.getRecordList());
                Logger.d("MsgMainFragment.MsgMainFragment.LOAD_MORE");
                break;
            case MsgMainFragment.LOAD_MORE_END:
                mAdapter.loadMoreEnd();
                Logger.d("MsgMainFragment.LOAD_MORE_END");
                break;
            case MsgMainFragment.UPDATE_AN_OLD_DATA:
                // TODO: 2017/6/6  单独刷新
                Record r = msg.getRecord();
                int indexOf = mAdapter.getData().indexOf(r);
                mAdapter.notifyItemChanged(indexOf);
                break;
            case MsgMainFragment.UPDATE_REFRESH:
                mAdapter.setNewData(msg.getRecordList());
                openRecordPosition = -1;
                break;
            case MsgMainFragment.DETAIL_DELETE_OIL:
                Record rec = msg.getRecord();
                mAdapter.getData().remove(rec);
                if (mAdapter.getData().size() == 0) {
                    mAdapter.setNewData(null);
                } else {
                    mAdapter.notifyItemRemoved(openRecordPosition);
                }
                openRecordPosition = -1;
                break;
            case MsgMainFragment.DETAIL_DELETE_MT:
                Record rec2 = msg.getRecord();
                mAdapter.getData().remove(rec2);
                if (mAdapter.getData().size() == 0) {
                    mAdapter.setNewData(null);
                } else {
                    mAdapter.notifyItemRemoved(openRecordPosition);
                }
                openRecordPosition = -1;
                break;


        }
    }

    //srl.setOnRefreshListener(this);
    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
//        getData(0, MsgMainFragment.REFRESH);
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(), 0, MsgMainFragment.REFRESH));
        pageCount = 1;
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
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(), pageCount, MsgMainFragment
                .LOAD_MORE));
        pageCount++;
    }

    //mAdapter.setOnItemChildClickListener(this);
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        final RecordDao dao = RecordDao.getInstance(mActivity);
        final Record record = mAdapter.getData().get(position);
        int id = view.getId();
        final int itemType = record.getItemType();
        switch (id) {
            //删除单条记录
            case R.id.it_iv_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.delete).setIcon(R.drawable.ic_delete_pressed).setMessage(R.string
                        .setting_oiltype_dialog_msg).setPositiveButton(R.string.confirm, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (itemType) {
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
                        //更新openRecordPosition位置
                        openRecordPosition = -1;
                        if (mAdapter.getData().size() == 0) {
                            mAdapter.setNewData(null);
                        } else {
                            mAdapter.notifyItemRemoved(position);
                        }
                        Toast.makeText(mActivity.getApplicationContext(), R.string.delete_sucess, Toast.LENGTH_SHORT)
                                .show();
                    }
                }).setNegativeButton(R.string.cancel, null).show();
                break;
            //修改数据
            case R.id.it_iv_update:
                switch (itemType) {
                    case Record.FLAG_MT:
                        MaintenanceFragment fragment = new MaintenanceFragment();
                        fragment.mt = record.getMt();
                        fragment.mRecord=record;
                        mActivity.switchFrag(this, fragment, false, null);
                        break;

                    case Record.FLAG_OIL:
                        OilFragment fragment1 = new OilFragment();
                        fragment1.mOil = record.getOil();
                        fragment1.mRecord = record;
                        mActivity.switchFrag(this, fragment1, false, null);
                        break;
                }

                break;
            //查看明细
            case R.id.it_iv_detail:
                switch (itemType) {
                    case Record.FLAG_MT:
                        // TODO: 2017/6/6 详情页面
                        DetailExpenseFragment fragment = new DetailExpenseFragment();
                        fragment.mRecord = record;
                        mActivity.switchFrag(this, fragment, false, null);
                        break;

                    case Record.FLAG_OIL:
                        DetailOilFragment detailOilFragment = new DetailOilFragment();
                        detailOilFragment.mRecord = record;
                        mActivity.switchFrag(this, detailOilFragment, false, null);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mActivity.hideKeyboard(recyclerView);
        }
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
