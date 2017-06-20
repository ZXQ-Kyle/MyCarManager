package com.kyle.mycar.Fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.TagLayout.TagContainerLayout;
import com.kyle.mycar.View.TagLayout.TagView;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Table.MtTag;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryExpenseFragment extends BaseFragment implements TagView.OnTagClickListener {

    private static final long PAGE_SIZE=15;

    @BindView(R.id.et_from_date_query)
    EditText etFrom;
    @BindView(R.id.et_to_date_query)
    EditText etTo;
    @BindView(R.id.tag_layout_query_expense)
    TagContainerLayout tagLayout;
    @BindView(R.id.iv_open_query)
    ImageView ivOpen;
    @BindView(R.id.tv_open_query)
    TextView tvOpen;
    @BindView(R.id.ll_open)
    LinearLayout llOpen;
    @BindView(R.id.tag_layout2_query_expense)
    TagContainerLayout tagLayoutDeleted;

    private boolean isOpen;
    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_query_expense, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.query_expense, 1, 0, null);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String strDate = format.format(time);
        etTo.setText(strDate);

        calendar.add(Calendar.DAY_OF_MONTH, -7);
        time = calendar.getTime();
        strDate = format.format(time);
        etFrom.setText(strDate);

        ArrayList<String> tagList = new ArrayList<>();
        MtTagDao tagDao = MtTagDao.getInstance(mActivity);
        List<MtTag> list = tagDao.queryAllButIsDelete("id", true);
        if (list != null) {
            for (MtTag tag : list) {
                tagList.add(tag.getTag());
            }
        }
        tagLayout.setTags(tagList);
        tagLayout.setOnTagClickListener(this);
    }


    @OnClick({R.id.et_from_date_query, R.id.et_to_date_query,R.id.ll_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_from_date_query:
                DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(MyConstant.QUERY_EXPENSE_FROM_DATE, -1);
                fragment.mTimeGone = true;
                fragment.show(getFragmentManager(), getString(R.string.query_expense));
                break;
            case R.id.et_to_date_query:
                DatePickerDialogFragment fragment1 = DatePickerDialogFragment.newInstance(MyConstant.QUERY_EXPENSE_TO_DATE, -1);
                fragment1.mTimeGone = true;
                fragment1.show(getFragmentManager(), getString(R.string.query_expense));
                break;
            case R.id.ll_open:
                isOpen=!isOpen;
                if (isOpen){
                    tagLayoutDeleted.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_query_clock);
                    ivOpen.startAnimation(animation);
                    animation=AnimationUtils.loadAnimation(mActivity, R.anim.set_query_in);
                    tagLayoutDeleted.startAnimation(animation);

                    ArrayList<String> tagList = new ArrayList<>();
                    List<MtTag> list = MtTagDao.getInstance(mActivity).query("isDelete", true, "id", true);
                    if (list != null) {
                        for (MtTag tag : list) {
                            tagList.add(tag.getTag());
                        }
                    }
                    tagLayoutDeleted.setTags(tagList);
                    tagLayoutDeleted.setOnTagClickListener(this);

                    tvOpen.setText(R.string.hide_delete_tag);
                }else {
                    tagLayoutDeleted.setVisibility(View.GONE);
                    Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_query_anticlock);
                    ivOpen.startAnimation(animation);
                    animation=AnimationUtils.loadAnimation(mActivity, R.anim.set_query_out);
                    tagLayoutDeleted.startAnimation(animation);

                    tvOpen.setText(R.string.show_delete_tag);
                }
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        switch (msg.getFlag()) {
            case MyConstant.QUERY_EXPENSE_FROM_DATE:
                etFrom.setText(msg.getMsg());
                break;
            case MyConstant.QUERY_EXPENSE_TO_DATE:
                etTo.setText(msg.getMsg());
                break;
        }
    }

    @Override
    public void onTagClick(int position, String text) {
        long from = MyDateUtils.strToLong(etFrom.getText().toString(),"yyyy年MM月dd日");
        long to = MyDateUtils.strToLong(etTo.getText().toString(),"yyyy年MM月dd日");
        if (from>to){
            Toast.makeText(mActivity, R.string.wrong_from_date, Toast.LENGTH_SHORT).show();
            return;
        }
        QueryExTagFragment fragment = QueryExTagFragment.newInstance(text, from, to);
        mActivity.switchFrag(this,fragment,false,null);
    }

    @Override
    public void onTagLongClick(int position, String text) {

    }

    @Override
    public void onTagCrossClick(int position) {

    }


}
