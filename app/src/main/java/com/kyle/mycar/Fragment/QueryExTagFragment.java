package com.kyle.mycar.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.Bean.MsgQuery;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.MyItemDecoration;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.Record;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryExTagFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "fromDate";
    private static final String ARG_PARAM3 = "toDate";

    private long pageCount = 0;
    private String mTitle;
    private long mFromDate;
    private long mToDate;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private QuickAdapter mAdapter = new QuickAdapter(null);

    public static QueryExTagFragment newInstance(String title, long fromDate, long toDate) {
        QueryExTagFragment fragment = new QueryExTagFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putLong(ARG_PARAM2, fromDate);
        args.putLong(ARG_PARAM3, toDate);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mFromDate = getArguments().getLong(ARG_PARAM2);
            mToDate = getArguments().getLong(ARG_PARAM3);
        }
    }

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_query_ex_tag, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.expense, 2, 0, null);
        mToolbar.setTitle(mTitle);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.addItemDecoration(new MyItemDecoration(mActivity));
        recyclerView.setAdapter(mAdapter);
        mActivity.mThreadPool.execute(new getDataRun(mTitle,mActivity,0,MyConstant.QUERY_EX_NEW_DATA, mFromDate, mToDate));
        pageCount++;
        srl.setOnRefreshListener(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MsgQuery msg) {
        switch (msg.flag) {
            case MyConstant.QUERY_EX_NEW_DATA:
                mAdapter.setNewData((List<Maintenance>) msg.object);
                mAdapter.notifyDataSetChanged();
                break;

            case MyConstant.QUERY_EX_LOAD_MORE:
                List<Maintenance> recordList = (List<Maintenance>) msg.object;
                if (recordList.size() == 0) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.addData(recordList);
                }
                break;
            case MyConstant.QUERY_EX_REFRESH:
                mAdapter.setNewData((List<Maintenance>) msg.object);
                mAdapter.setEnableLoadMore(true);
                srl.setRefreshing(false);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MsgMainFragment msg) {
        switch (msg.getFlag()) {
            case MsgMainFragment.UPDATE_AN_OLD_DATA:
                Maintenance m = msg.getRecord().getMt();
                int indexOf = mAdapter.getData().indexOf(m);
                mAdapter.notifyItemChanged(indexOf);
                break;
            case MsgMainFragment.DETAIL_DELETE_OIL:
                Maintenance mt = msg.getRecord().getMt();
                int index = mAdapter.getData().indexOf(mt);
                mAdapter.notifyItemRemoved(index);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DetailExpenseFragment fragment = new DetailExpenseFragment();
        Maintenance mt = (Maintenance) adapter.getData().get(position);
        List<Record> list = RecordDao.getInstance(mActivity).query(Record.COLUMN_MT_ID, mt, "date", false);
        if (null!=list && list.size()>0){
            fragment.mRecord =list.get(0);
        }
        mActivity.switchFrag(this, fragment, false, null);
    }

    @Override
    public void onLoadMoreRequested() {
        mActivity.mThreadPool.execute(new getDataRun(mTitle,mActivity,pageCount,MyConstant.QUERY_EX_LOAD_MORE, mFromDate, mToDate));
        pageCount++;
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        mActivity.mThreadPool.execute(new getDataRun(mTitle,mActivity,0,MyConstant.QUERY_EX_REFRESH, mFromDate, mToDate));
        pageCount = 1;
    }

    private static class QuickAdapter extends BaseQuickAdapter<Maintenance, BaseViewHolder> {
        private static final String RMB = "¥ ";
        private static final String km = "km";

        public QuickAdapter(@Nullable List<Maintenance> data) {
            super(R.layout.item_recycle_query_expense, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Maintenance item) {
            helper.setText(R.id.tv_date, MyDateUtils.longToStr(item.getDate()))
                    .setText(R.id.tv_money, RMB + item.getMoney())
                    .setText(R.id.tv_odo, item.getOdometer() + km)
                    .setText(R.id.tv_tag,item.getTags());
        }
    }

    private static class getDataRun implements Runnable {
        private Context mContext;
        private long mOff;
        private int mWhat;
        private long mFrom;
        private long mTo;
        private String mText;
        /**
         * @param text
         * @param off  刷新第一页，为0，其余设置pageCount
         * @param what 获取数据后返回
         */
        public getDataRun(String text, Context context, long off, int what, long from, long to) {
            mContext = context.getApplicationContext();
            mOff = off;
            mWhat = what;
            mFrom = from;
            mTo = to+86400000;
            mText="%"+text+"%";
        }
        @Override
        public void run() {

            MtDao dao = MtDao.getInstance(mContext);

            try {
                List<Maintenance> query = dao.queryBuilder().
                        offset(MyConstant.PAGE_SIZE * mOff).limit(MyConstant.PAGE_SIZE).orderBy("date", false)
                        .where().between("date", mFrom, mTo).and()
                        .eq("isDelete", false).and()
                        .like("tags", mText)
                        .query();

                EventBus.getDefault().post(new MsgQuery(mWhat, query));
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

}
