package com.kyle.mycar.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.kyle.mycar.Bean.MsgSetting;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Dao.OilTypeDao;
import com.kyle.mycar.db.Table.MtTag;
import com.kyle.mycar.db.Table.OilType;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTypeOrTagFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private boolean isOilType=true;

    @BindView(R.id.et_setting_add_oiltype)
    AppCompatEditText et;

    public OilType mOilType;
    public MtTag mTag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isOilType = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View initView() {
        noEventBus=true;
        return View.inflate(mActivity, R.layout.fragment_oil_type_add, null);
    }

    public static AddTypeOrTagFragment newInstance(boolean isOilType) {
        AddTypeOrTagFragment fragment = new AddTypeOrTagFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1,isOilType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        if (isOilType){
            initToolbar(R.string.oil_type, 2, R.menu.toolbar_confirm, this);
            if (null!=mOilType){
                et.setText(mOilType.getOilType());
            }
        }else {
            initToolbar(R.string.expense, 2, R.menu.toolbar_confirm, this);
            if (null!=mTag){
                et.setText(mTag.getTag());
            }
        }
        et.requestFocus();
        mActivity.showKeyboard(et);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.menu_confirm){
            String text = et.getText().toString().trim();
            if (TextUtils.isEmpty(text)){
                Toast.makeText(mActivity, R.string.err_empty_odo, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (isOilType){
                if (null==mOilType){
                    //新建数据
                    OilType oilType = new OilType(text);
                    OilTypeDao.getInstance(mActivity.getApplicationContext()).add(oilType);
                    EventBus.getDefault().post(new MsgSetting(MyConstant.SETTING_ADD_OIL_TYPE,oilType));
                    Toast.makeText(mActivity, R.string.sucess, Toast.LENGTH_SHORT).show();
                }else if (!TextUtils.equals(text, mOilType.getOilType())){
                    //修改数据
                    mOilType.setOilType(text);
                    OilTypeDao.getInstance(mActivity.getApplicationContext()).update(mOilType);
                    EventBus.getDefault().post(new MsgSetting(MyConstant.SETTING_UPDATE_OIL_TYPE,mOilType));
                    Toast.makeText(mActivity, R.string.update_sucess, Toast.LENGTH_SHORT).show();
                }
            }else {
                //tag界面逻辑
                if (null==mTag){
                    //新建数据
                    MtTag mtTag = new MtTag(text);
                    MtTagDao.getInstance(mActivity.getApplicationContext()).add(mtTag);
                    EventBus.getDefault().post(new MsgSetting(MyConstant.SETTING_ADD_TAG,mtTag));
                    Toast.makeText(mActivity, R.string.sucess, Toast.LENGTH_SHORT).show();
                }else if (!TextUtils.equals(text, mTag.getTag())){
                    //修改数据
                    mTag.setTag(text);
                    MtTagDao.getInstance(mActivity.getApplicationContext()).update(mTag);
                    EventBus.getDefault().post(new MsgSetting(MyConstant.SETTING_UPDATE_TAG,mTag));
                    Toast.makeText(mActivity, R.string.update_sucess, Toast.LENGTH_SHORT).show();
                }
            }

            //没有修改数据直接返回
            mActivity.onBackPressed();
            return true;
        }else {
            Toast.makeText(mActivity, R.string.fail, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        mActivity.hideKeyboard(et);
        super.onDestroyView();
    }
}
