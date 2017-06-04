package com.kyle.mycar.Fragment;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyle.mycar.Bean.MsgChart;
import com.kyle.mycar.R;
import com.kyle.mycar.View.MyItemDecoration;
import com.kyle.mycar.db.Dao.OilTypeDao;
import com.kyle.mycar.db.Table.OilType;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;
import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingOilTypeFrag extends BaseFragment implements Toolbar.OnMenuItemClickListener,  BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_setting_oil_type, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.oil_type, 2, R.menu.toolbar_setting_oiltype, this);
        OilTypeDao typeDao = OilTypeDao.getInstance(mActivity);
        List<OilType> list = typeDao.queryAllButIsDelete("id", true);
        QuickAdapter mAdapter = new QuickAdapter(mActivity,list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.isFirstOnly(false);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration(mActivity));
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void no(MsgChart msgChart) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_delete_setting_oiltype:
                Toast.makeText(mActivity, "ssss", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_update_setting_oiltype:
                Toast.makeText(mActivity, "sss222s", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    static class QuickAdapter extends BaseQuickAdapter<OilType, BaseViewHolder> {
        private int[] colors;

        public QuickAdapter(Context context, @Nullable List<OilType> data) {
            super(R.layout.item_recycleview_setting, data);
            colors = context.getResources().getIntArray(R.array.color);
        }

        @Override
        protected void convert(BaseViewHolder helper, OilType item) {
            char c = item.getOilType().charAt(0);
            int length = colors.length;
            int i = item.getId()%length;
            TextDrawable drawable1 = TextDrawable.builder().buildRound(String.valueOf(c), colors[i]);
            helper.setText(R.id.it_tv_setting_oiltype, item.getOilType())
                    .setImageDrawable(R.id.it_iv_setting_oiltype, drawable1)
                    .addOnClickListener(R.id.iv_delete_setting_oiltype)
                    .addOnClickListener(R.id.iv_update_setting_oiltype);
        }
    }

}
