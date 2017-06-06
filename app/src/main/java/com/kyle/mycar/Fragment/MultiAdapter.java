package com.kyle.mycar.Fragment;

import android.support.v7.widget.ThemedSpinnerAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Headers;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.Record;

import java.util.List;

/**
 * Created by Zhang on 2017/5/25.
 */

public class MultiAdapter extends BaseMultiItemQuickAdapter<Record,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultiAdapter(List<Record> data) {
        super(data);
        addItemType(Record.FLAG_OIL, R.layout.item_recycle_history);
        addItemType(Record.FLAG_MT, R.layout.item_recycle_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {
        String rmb = mContext.getString(R.string.RMB);
        String km = mContext.getString(R.string.km);
        helper.setVisible(R.id.it_iv_delete,item.isVisible)
                .setVisible(R.id.it_iv_update,item.isVisible)
                .setVisible(R.id.it_iv_detail,item.isVisible)
                .addOnClickListener(R.id.it_iv_delete)
                .addOnClickListener(R.id.it_iv_update);

        int itemType = item.getItemType();
        switch (itemType) {
            case Record.FLAG_OIL:
                Glide.with(mContext).load(R.drawable.odo).into((ImageView) helper.getView(R.id.it_iv_h_odo));
                Glide.with(mContext).load(R.drawable.oil_list).into((ImageView) helper.getView(R.id.it_iv_history));
                Oil oil = item.getOil();
                helper.setText(R.id.it_tv_history_date, MyDateUtils.longToStr(oil.getDate()))
                        .setText(R.id.it_tv_history_odo, oil.getOdometer()+km)
                        .setText(R.id.it_tv_history_money, rmb+oil.getMoney())
                        .setText(R.id.it_tv_h_tags,R.string.add_oil);
                break;
            case Record.FLAG_MT:
                Maintenance mt = item.getMt();
                Glide.with(mContext).load(R.drawable.expense).into((ImageView) helper.getView(R.id.it_iv_history));
                Glide.with(mContext).load(R.drawable.odo).into((ImageView) helper.getView(R.id.it_iv_h_odo));
                helper.setText(R.id.it_tv_history_date, MyDateUtils.longToStr(mt.getDate()))
                        .setText(R.id.it_tv_history_odo, mt.getOdometer()+km)
                        .setText(R.id.it_tv_history_money, rmb+mt.getMoney())
                        .setText(R.id.it_tv_h_tags,mt.getTags());
                break;

        }
    }
}
