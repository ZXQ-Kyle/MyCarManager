package com.kyle.mycar.Fragment;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.Bean.MsgQuery;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.MyItemDecoration;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.Record;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter
        .RequestLoadMoreListener {

    @BindView(R.id.et_from_date_query)
    EditText etFrom;
    @BindView(R.id.et_to_date_query)
    EditText etTo;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    Unbinder unbinder;
    public static final int PAGE_SIZE = 10;
    private long pageCount = 0;
    private QuickAdapter mAdapter = new QuickAdapter(null);

    @Override
    public View initView() {

        return View.inflate(mActivity, R.layout.fragment_query, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.query, 1, 0, null);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String strDate = format.format(time);
        etTo.setText(strDate);

        calendar.add(Calendar.DAY_OF_MONTH, -7);
        time = calendar.getTime();
        strDate = format.format(time);
        etFrom.setText(strDate);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        mActivity.mThreadPool.execute(new getDataRun(mActivity.getApplicationContext(), 0, MyConstant.QUERY_SET_ADAPTER));
        recyclerView.addItemDecoration(new MyItemDecoration(mActivity));
        recyclerView.setAdapter(mAdapter);
    }


    @OnClick({R.id.et_from_date_query, R.id.et_to_date_query})
    public void onViewClicked(View view) {
        DatePickerDialogFragment fragment = null;

        switch (view.getId()) {
            case R.id.et_from_date_query:
                fragment = DatePickerDialogFragment.newInstance(MyConstant.QUERY_FROM_DATE, -1);
                break;
            case R.id.et_to_date_query:
                fragment = DatePickerDialogFragment.newInstance(MyConstant.QUERY_TO_DATE, -1);
                break;
        }
        fragment.mTimeGone = true;
        fragment.show(getFragmentManager(), getString(R.string.query));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        switch (msg.getFlag()) {
            case MyConstant.QUERY_FROM_DATE:
                etFrom.setText(msg.getMsg());
                break;

            case MyConstant.QUERY_TO_DATE:
                etTo.setText(msg.getMsg());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MsgQuery msg) {
        switch (msg.flag) {
            case MyConstant.QUERY_SET_ADAPTER:

                mAdapter.setNewData((List<Record>) msg.object);


                break;

        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onLoadMoreRequested() {

    }

    private static class QuickAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {
        private static final String RMB = "¥ ";
        private static final String L = "L";
        private static final String km = "km";

        public QuickAdapter(@Nullable List<Record> data) {
            super(R.layout.item_recycle_query_oil, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Record item) {
            Oil oil = item.getOil();

            helper.setText(R.id.tv_date, MyDateUtils.longToStr(oil.getDate())).setText(R.id.tv_money, RMB + oil
                    .getMoney()).setText(R.id.tv_odo, oil.getOdometer() + km).setText(R.id.tv_price, RMB + oil
                    .getPrice()).setText(R.id.tv_type, oil.getOilType()).setText(R.id.tv_quantity, oil.getQuantity()
                    + L);


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
//            pageCount = off + 1;
            RecordDao dao = RecordDao.getInstance(mContext);
//            long count = dao.countOf();
//            long maxLine = count - mOff * PAGE_SIZE;
//            if (maxLine <= 0) {
//                //数据到底了，并且是上拉加载更多的情况,或者初次进入无数据
//                if (mWhat == MsgMainFragment.LOAD_MORE) {
//                    EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.LOAD_MORE_END));
//                } else if (mWhat == MsgMainFragment.SET_ADAPTER) {
//                    EventBus.getDefault().post(new MsgMainFragment(mWhat));
//                }
//            } else {
//                List<Record> beanList = dao.queryOffestLimit(count - maxLine, PAGE_SIZE);
//                EventBus.getDefault().post(new MsgMainFragment(mWhat, beanList));
//            }


            try {
                List<Record> query = dao.queryBuilder().offset(0l).limit(2l).orderBy("date", false).where().isNull
                        (Record.COLUMN_MT_ID).and().eq("isDelete", false).query();

                EventBus.getDefault().post(new MsgQuery(mWhat, query));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}