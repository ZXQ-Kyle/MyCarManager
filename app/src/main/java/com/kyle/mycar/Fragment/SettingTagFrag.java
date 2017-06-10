package com.kyle.mycar.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyle.mycar.Bean.MsgSetting;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.R;
import com.kyle.mycar.View.MyItemDecoration;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Table.MtTag;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingTagFrag extends BaseFragment implements Toolbar.OnMenuItemClickListener, BaseQuickAdapter
        .OnItemChildClickListener {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private List<MtTag> mList;
    private QuickAdapter mAdapter;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_setting_oil_type, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.expense, 2, R.menu.toolbar_add, this);
        MtTagDao dao = MtTagDao.getInstance(mActivity);
        mList = dao.queryAllButIsDelete("id", true);
        mAdapter = new QuickAdapter(mActivity, mList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.isFirstOnly(false);
        mAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration(mActivity));
        // TODO: 2017/6/5         //添加下拉刷新
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MsgSetting msg) {
        int flag = msg.flag;
        if (flag == MyConstant.SETTING_ADD_TAG) {
            mAdapter.addData((MtTag) msg.object);
            mAdapter.notifyItemInserted(mAdapter.getData().size());
        } else if (flag == MyConstant.SETTING_UPDATE_TAG) {
            MtTag type = (MtTag) msg.object;
            int indexOf = mAdapter.getData().indexOf(type);
            mAdapter.notifyItemChanged(indexOf);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            mActivity.switchFrag(this, AddTypeOrTagFragment.newInstance(false), false, null);
            return true;
        }
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()) {
            case R.id.iv_delete_setting_oiltype:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.delete).setIcon(R.drawable.ic_delete_pressed).setMessage(R.string
                        .setting_oiltype_dialog_msg).setPositiveButton(R.string.confirm, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MtTag tag = mList.get(position);
                        tag.setDelete(true);
                        MtTagDao.getInstance(mActivity.getApplicationContext()).update(tag);
                        mAdapter.getData().remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                }).setNegativeButton(R.string.cancel, null).show();
                break;

            case R.id.iv_update_setting_oiltype:
                AddTypeOrTagFragment fragment = AddTypeOrTagFragment.newInstance(false);
                fragment.mTag = mList.get(position);
                mActivity.switchFrag(this, fragment, false, null);
                break;
        }
    }

   private static class QuickAdapter extends BaseQuickAdapter<MtTag, BaseViewHolder> {
        private int[] colors;

        public QuickAdapter(Context context, @Nullable List<MtTag> data) {
            super(R.layout.item_recycleview_setting, data);
            colors = context.getResources().getIntArray(R.array.color);
        }

        @Override
        protected void convert(BaseViewHolder helper, MtTag item) {
            String tag = item.getTag();
            char c;
            if (TextUtils.isEmpty(tag)) {
                c = 'c';
            } else {
                c = tag.charAt(0);
            }
            int length = colors.length;
            int i = item.getId() % length;
            TextDrawable drawable1 = TextDrawable.builder().buildRound(String.valueOf(c), colors[i]);
            helper.setText(R.id.it_tv_setting_oiltype, item.getTag()).setImageDrawable(R.id.it_iv_setting_oiltype, 
                    drawable1).addOnClickListener(R.id.iv_delete_setting_oiltype).addOnClickListener(R.id
                    .iv_update_setting_oiltype);
        }
    }

}
