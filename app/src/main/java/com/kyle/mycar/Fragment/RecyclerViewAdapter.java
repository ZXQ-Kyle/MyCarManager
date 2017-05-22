package com.kyle.mycar.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyle.mycar.Bean.ItemTagBean;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Table.MtMap;

import java.util.List;

/**
 * Created by Zhang on 2017/5/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ItemTagBean> mList;

    public RecyclerViewAdapter(List list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemTagBean bean = mList.get(position);
        holder.date.setText(bean.getDate());
        holder.money.setText("¥" + bean.getMoney());
        StringBuilder sb = new StringBuilder();
        List<MtMap> tags = bean.getTags();
        if (tags!=null && tags.size()>0){
            for (int i = 0; i < tags.size(); i++) {
                sb.append(tags.get(i).getTag());
                if (i<tags.size()-1){
                    sb.append("，");
                }
            }
        }
        holder.tags.setText(sb);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView money;
        private TextView tags;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.it_tv_history_date);
            money = (TextView) itemView.findViewById(R.id.it_tv_history_money);
            tags = (TextView) itemView.findViewById(R.id.it_tv_history_tags);
        }
    }
}
