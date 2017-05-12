package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackuhan.flowlayouttags.FlowlayoutTags;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.GlobalConstant;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends BaseFragment {


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
//        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
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
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.about));
        list.add(getString(R.string.maintenance));
        list.add(getString(R.string.confirm));
        list.add(getString(R.string.hello_blank_fragment));
        list.add(getString(R.string.action_settings));
        list.add(getString(R.string.navigation_drawer_close));
        list.add(getString(R.string.oil));
        list.add(getString(R.string.oil));
        refreshCategorys(tagsMt, list);
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
                DatePickerDialogFragment dialogFragment =
                        DatePickerDialogFragment.newInstance(GlobalConstant.EX_FRAGMENT_RETURN_DATE
                                ,GlobalConstant.EX_FRAGMENT_RETURN_TIME);
                dialogFragment.show(getFragmentManager(), "mtDate");
                break;
            case R.id.btn_confirm:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDateMessage(MessageEvent msg) {
        switch (msg.getFlag()) {
            case GlobalConstant.EX_FRAGMENT_RETURN_DATE:
                mDate = msg.getMsg() + mDate.substring(11);
                break;
            case GlobalConstant.EX_FRAGMENT_RETURN_TIME:
                mDate = mDate.substring(0, 13) + msg.getMsg();
                break;
        }
        iaeMtDate.setText(mDate);
    }
}
