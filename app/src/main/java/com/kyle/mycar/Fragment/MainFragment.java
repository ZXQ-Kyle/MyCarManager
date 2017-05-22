package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kyle.mycar.Bean.ItemTagBean;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.MtMapDao;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtMap;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
    RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<ItemTagBean> mTagBeens = new ArrayList<>();

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
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        getData(MsgMainFragment.SET_ADAPTER);
//        recyclerView.setLinearLayout();

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


    private void getData(final int what) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mTagBeens.clear();
                MtDao mtDao = MtDao.getInstance(mActivity);
                List<Maintenance> mt = mtDao.queryAllButIsDelete("date", true);
                MtMapDao mapDao = MtMapDao.getInstance(mActivity);
                for (Maintenance m : mt) {
                    ItemTagBean bean = new ItemTagBean();
                    bean.setDate(MyDateUtils.longToStr(m.getDate()));
                    bean.setMoney(m.getMoney());
                    List<MtMap> list = mapDao.queryButIsDelete("mt_id", m.getId(), "id", true);
                    bean.setTags(list);
                    mTagBeens.add(bean);
                }
                EventBus.getDefault().post(new MsgMainFragment(what));
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
    public void msg(MsgMainFragment msg){

        switch (msg.getFlag()){
            case MsgMainFragment.SET_ADAPTER:
                mAdapter = new RecyclerViewAdapter(mTagBeens);
                recyclerView.setAdapter(mAdapter);
                break;
            case MsgMainFragment.ADAPTER_NOTIFY_CHANGE:
                mAdapter.notifyDataSetChanged();
                break;
            case MsgMainFragment.UPDATE_DATA:
                getData(MsgMainFragment.ADAPTER_NOTIFY_CHANGE);
                break;

        }
    }

}
