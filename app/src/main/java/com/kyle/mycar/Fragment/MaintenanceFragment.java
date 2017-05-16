package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.dao.BaseForeignCollection;
import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.misc.TransactionManager;
import com.jackuhan.flowlayouttags.FlowlayoutTags;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.GlobalConstant;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.MtMapDao;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtMap;
import com.kyle.mycar.db.Table.MtTag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceFragment extends BaseFragment {


    @BindView(R.id.iae_mt_date)
    ImgAndEtView iaeMtDate;
    @BindView(R.id.iae_mt_odometer)
    ImgAndEtView iaeMtOdometer;
    @BindView(R.id.iae_mt_money)
    ImgAndEtView iaeMtMoney;

    Unbinder unbinder;
    @BindView(R.id.tags_mt)
    FlowlayoutTags tagsMt;
    private String mDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void initData() {

        //设置日期默认为当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        mDate = sdf.format(date);
        iaeMtDate.setText(mDate);

        iaeMtDate.setUnEditable();
        iaeMtMoney.setInputTypeOfNumber();
        iaeMtOdometer.setInputTypeOfNumber();

        //初始化Tags
        initTags();

    }

    private void initTags() {
        ArrayList<String> tagList = new ArrayList<>();
        MtTagDao tagDao = MtTagDao.getInstance(mActivity);
        List<MtTag> list = tagDao.queryAllButIsDelete("id", true);
        if (list!=null){
            for (MtTag tag : list) {
                tagList.add(tag.getTag());
            }
        }

        refreshCategorys(tagsMt, tagList);
        tagsMt.setOnTagChangeListener(new FlowlayoutTags.OnTagChangeListener() {
            @Override
            public void onAppend(FlowlayoutTags flowlayoutTags, String tag) {
                Snackbar.make(getView(), "onAppend" + tag, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onDelete(FlowlayoutTags flowlayoutTags, String tag) {
                Snackbar.make(getView(), "onDelete" + tag, Snackbar.LENGTH_LONG).show();
            }
        });
        tagsMt.setOnTagClickListener(new FlowlayoutTags.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {

                String[] strings = tagsMt.getCheckedTagsText();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < strings.length; i++) {
                    sb.append(strings[i]).append(",");
                }
                Snackbar.make(getView(), "onTagClick:" + sb.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void refreshCategorys(FlowlayoutTags flowlayoutTags, List<String> list) {
        flowlayoutTags.removeAllViews();
        flowlayoutTags.setTags(list);
        flowlayoutTags.setTagsUncheckedColorAnimal(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.iae_mt_date, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iae_mt_date:
                DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(GlobalConstant
                        .MT_FRAGMENT_RETURN_DATE, GlobalConstant.MT_FRAGMENT_RETURN_TIME);
                dialogFragment.show(getFragmentManager(), "mtDate");
                break;
            case R.id.btn_confirm:
                saveData();
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }
    //事务保存
    private void transactionSave() {
        long date = MyDateUtils.strToLong(mDate);
        String money = iaeMtMoney.getText();
        String odometer = iaeMtOdometer.getText();
        String[] tagsText = tagsMt.getCheckedTagsText();
        MtDao mtDao = MtDao.getInstance(mActivity);
        MtMapDao mapDao = MtMapDao.getInstance(mActivity);

        Maintenance mt = new Maintenance(date, money, odometer);
        mtDao.add(mt);
        for (int i = 0; i < tagsText.length; i++) {
            mapDao.add(new MtMap(mt,tagsText[i]));
        }
    }

    private void saveData() {
        //事务保存
        TransactionManager manager = new TransactionManager(DbOpenHelper.getInstance(mActivity).getConnectionSource());
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                transactionSave();
                return true;
            }
        };

        try {
            manager.callInTransaction(callable);
            Snackbar.make(getView(),getString(R.string.sucess),Snackbar.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Snackbar.make(getView(),getString(R.string.fail),Snackbar.LENGTH_LONG).show();
        }

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDateMessage(MessageEvent msg) {
        switch (msg.getFlag()) {
            case GlobalConstant.MT_FRAGMENT_RETURN_DATE:
                mDate = msg.getMsg() + mDate.substring(11);
                break;
            case GlobalConstant.MT_FRAGMENT_RETURN_TIME:
                mDate = mDate.substring(0, 13) + msg.getMsg();
                break;
        }
        iaeMtDate.setText(mDate);
    }
}
