package com.kyle.mycar.Fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.j256.ormlite.misc.TransactionManager;
import com.jackuhan.flowlayouttags.FlowlayoutTags;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtTag;
import com.kyle.mycar.db.Table.Record;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {


    @BindView(R.id.iae_mt_date)
    ImgAndEtView iaeMtDate;
    @BindView(R.id.iae_mt_odometer)
    ImgAndEtView iaeMtOdometer;
    @BindView(R.id.iae_mt_money)
    ImgAndEtView iaeMtMoney;
    @BindView(R.id.tags_mt)
    FlowlayoutTags tagsMt;

    private String mDate;
    public Maintenance mt;
    public Record mRecord;


    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_maintenance, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.expense, 2, R.menu.toolbar_confirm, this);
        //设置日期默认为当前时间
        mDate = MyDateUtils.longToStr(System.currentTimeMillis());
        iaeMtDate.setText(mDate);

        iaeMtDate.setUnEditable();
        iaeMtOdometer.setInputTypeOfNumber();
        iaeMtMoney.setInputTypeOfNumberDecimal();
        //初始化Tags
        initTags();
        iaeMtOdometer.rqFocus();
        mActivity.showKeyboard(iaeMtOdometer);
    }

    private void initTags() {
        ArrayList<String> tagList = new ArrayList<>();
        MtTagDao tagDao = MtTagDao.getInstance(mActivity);
        List<MtTag> list = tagDao.queryAllButIsDelete("id", true);
        if (list != null) {
            for (MtTag tag : list) {
                tagList.add(tag.getTag());
            }
        }
        refreshCategorys(tagsMt, tagList);

        //初始化数据
        if (mt != null) {
            mDate = MyDateUtils.longToStr(mt.getDate());
            iaeMtDate.setText(mDate);
            iaeMtMoney.setText(mt.getMoney());
            iaeMtOdometer.setText(mt.getOdometer());
            String tags = mt.getTags();
            String[] split = tags.split(",");
            int length = split.length;
            int index = -1;
            int childCount=-1;
            for (int i = 0; i < length; i++) {
                index = tagList.indexOf(split[i]);
                FlowlayoutTags.TagView tagView=null;
                if (index >= 0) {
                    tagView = (FlowlayoutTags.TagView) tagsMt.getChildAt(index);
                    tagView.setCheckedWithoutAnimal(true);
                    index = -1;
                } else {
                    childCount = tagsMt.getChildCount();
                    tagsMt.appendTag(split[i]);
                    tagView = (FlowlayoutTags.TagView) tagsMt.getChildAt(childCount);
                    tagView.setCheckedWithoutAnimal(true);
                }
            }
        }

    }

    public void refreshCategorys(FlowlayoutTags flowlayoutTags, List<String> list) {
        flowlayoutTags.removeAllViews();
        flowlayoutTags.setTags(list);
        flowlayoutTags.setTagsUncheckedColorAnimal(false);
    }

    @OnClick({R.id.iae_mt_date})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iae_mt_date:
                //打开时间选择对话框
                DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(MyConstant
                        .MT_FRAGMENT_RETURN_DATE, MyConstant.MT_FRAGMENT_RETURN_TIME);
                dialogFragment.show(getFragmentManager(), "mtDate");
                break;
//            case R.id.iv_mt:
//                //点击打开修改tag界面
//                mActivity.switchFrag(this,new SettingTagFrag(),false,null);
//                // TODO: 2017/6/5 消息回传
//                break;
                   }

    }

    //事务保存
    private void transactionSave() {
        long date = MyDateUtils.strToLong(mDate);
        String money = iaeMtMoney.getText();
        String odometer = iaeMtOdometer.getText();
        String[] tagsText = tagsMt.getCheckedTagsText();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tagsText.length; i++) {
            sb.append(tagsText[i]);
            if (i < tagsText.length - 1) {
                sb.append(",");
            }
        }
        MtDao mtDao = MtDao.getInstance(mActivity);
        RecordDao recordDao = RecordDao.getInstance(mActivity);
        if (mt == null) {
            Maintenance mt2 = new Maintenance(date, money, odometer, sb.toString());
            mtDao.add(mt2);
            mRecord=new Record(mt2,date);
            recordDao.add(mRecord);
        } else {

            mt.setMoney(money);
            mt.setOdometer(odometer);
            mt.setTags(sb.toString());
            mt.setDate(date);
            mtDao.update(mt);

            if (mRecord.getDate() != date) {
                mRecord.setDate(date);
                recordDao.update(mRecord);
            }
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
//            Toast.makeText(mActivity, R.string.sucess, Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
//            Toast.makeText(mActivity, R.string.fail, Toast.LENGTH_LONG).show();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDateMessage(MessageEvent msg) {
        switch (msg.getFlag()) {
            case MyConstant.MT_FRAGMENT_RETURN_DATE:
                mDate = msg.getMsg() + mDate.substring(11);
                break;
            case MyConstant.MT_FRAGMENT_RETURN_TIME:
                mDate = mDate.substring(0, 13) + msg.getMsg();
                break;
        }
        iaeMtDate.setText(mDate);
    }

    //toolbar menu点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_confirm) {
            String odometer = iaeMtOdometer.getText();
            if (TextUtils.isEmpty(odometer)) {
                Toast.makeText(mActivity.getApplicationContext(), R.string.err_empty_odo, Toast.LENGTH_LONG).show();
                iaeMtOdometer.rqFocus();
                mActivity.showKeyboard(iaeMtOdometer);
                return true;
            }
            odometer=null;
            String money = iaeMtMoney.getText();
            if (TextUtils.isEmpty(money)) {
                Toast.makeText(mActivity.getApplicationContext(), R.string.err_empty_money, Toast.LENGTH_LONG).show();
                iaeMtMoney.rqFocus();
                mActivity.showKeyboard(iaeMtMoney);
                return true;
            }
            money=null;
            String[] tagsText = tagsMt.getCheckedTagsText();
            if (tagsText.length==0) {
                Toast.makeText(mActivity.getApplicationContext(), R.string.err_empty_tag, Toast.LENGTH_LONG).show()
                ;return true;
            }
            mActivity.mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    saveData();

                    if (null != mt) {
                        //更新数据
                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.UPDATE_AN_OLD_DATA,mRecord));
                    } else {
                        //新建数据
                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.UPDATE_AN_NEW_ONE_DATA,mRecord));
                    }
                    mActivity.onBackPressed();
                }
            });
            return true;
        }
        return false;
    }

}
