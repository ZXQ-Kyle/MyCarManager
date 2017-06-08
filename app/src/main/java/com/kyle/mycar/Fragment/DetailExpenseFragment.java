package com.kyle.mycar.Fragment;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.NumberAnimTextView;
import com.kyle.mycar.View.TagLayout.TagContainerLayout;
import com.kyle.mycar.db.Dao.MtDao;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.Record;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailExpenseFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, DialogInterface
        .OnClickListener {


    @BindView(R.id.tv_money_expense_detail)
    NumberAnimTextView tvMoney;
    @BindView(R.id.tv_odo_expense_detail)
    TextView tvOdo;
    @BindView(R.id.tv_date_expense_detail)
    TextView tvDate;
    @BindView(R.id.tag_layout_detail)
    TagContainerLayout tagLayout;

    public Record mRecord;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_detail_expense, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.expense, 2, R.menu.toolbar_detail, this);
        setDate();


    }

    private void setDate() {
        if (null != mRecord) {
            Maintenance mt = mRecord.getMt();
            tvMoney.setNumberString(mt.getMoney());
            tvMoney.setPrefixString(getResources().getString(R.string.RMB));
            tvMoney.setDuration(1000);

            tvOdo.setText(mt.getOdometer() + getString(R.string.km));
            tvDate.setText(MyDateUtils.longToStr(mt.getDate()));

            String[] strings = mt.getTags().split(",");
            tagLayout.setTags(strings);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.delete).setIcon(R.drawable.ic_delete_pressed).setMessage(R.string
                        .setting_oiltype_dialog_msg).setPositiveButton(R.string.confirm, this).setNegativeButton(R
                        .string.cancel, null).show();
                break;
            case R.id.menu_update:
                MaintenanceFragment fragment1 = new MaintenanceFragment();
                fragment1.mt = mRecord.getMt();
                fragment1.mRecord = mRecord;
                mActivity.switchFrag(this, fragment1, false, null);
                break;
        }
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
            //删除逻辑
            Maintenance mt = mRecord.getMt();
            mt.setDelete(true);
            MtDao.getInstance(mActivity.getApplicationContext()).update(mt);

            mRecord.setDelete(true);
            RecordDao.getInstance(mActivity.getApplicationContext()).update(mRecord);
            Toast.makeText(mActivity.getApplicationContext(), R.string.delete_sucess, Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.DETAIL_DELETE_MT, mRecord));
            mActivity.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgMainFragment msg) {
        if (msg.getFlag() == MsgMainFragment.UPDATE_AN_OLD_DATA) {
            mRecord = msg.getRecord();
            setDate();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mActivity.hideKeyboard(tvMoney);
    }

}
