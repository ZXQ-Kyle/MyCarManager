package com.kyle.mycar.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.kyle.mycar.Bean.MsgChart;
import com.kyle.mycar.R;
import com.kyle.mycar.View.MyMarkerView;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Record;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends BaseFragment {


    @BindView(R.id.tv_chart_title)
    TextView tvChartTitle;
    @BindView(R.id.mChart)
    LineChart mChart;
    Unbinder unbinder;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_chart, null);
        return view;
    }

    @Override
    public void initData() {
        Thread thread = new Thread(new getChartData(mActivity));
        thread.start();
        initToolbar(R.string.chart, R.color.Blue100, R.color.Blue300, 1);
        initChart();
    }


    private void initChart() {
        //设置表名
//        Description description = new Description();
//        description.setTextSize(15);
//        description.setText("油耗表");
//        mChart.setDescription(description);
        mChart.setNoDataText("无数据显示");
        //触摸，拖曳
        mChart.setTouchEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDragEnabled(true);
        //摩擦系数，滑动惯性
        mChart.setDragDecelerationEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        //点击显示数值view
//        MyMarkerView markerView = new MyMarkerView(mActivity, R.layout.maker_view);
//        markerView.setChartView(mChart);
//        mChart.setMarker(markerView);

        //左下角图例
        Legend legend = mChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.colorAccent));
        legend.setForm(Legend.LegendForm.CIRCLE);

        //设置X轴
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.enableGridDashedLine(10, 10, 0f);
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new DateValueFormatter());
        xAxis.setSpaceMax(100);
        xAxis.setLabelRotationAngle(25);

        //y轴设置
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.enableGridDashedLine(10, 10, 0);
        yAxis.setSpaceTop(35);
        yAxis.setGranularity(0.5f);
        //关闭右侧Y轴线
        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);
        //限制线
        LimitLine limitLine = new LimitLine(10.5f, "平均油耗 10.5");
//        limitLine.setLineColor(getResources().getColor(R.color.red));
        limitLine.enableDashedLine(20, 20, 0);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLine.setTextSize(10);
        limitLine.setLineWidth(4f);
        yAxis.addLimitLine(limitLine);

//        //添加数据
        mChart.setData(null);
        //单页最多显示值，必须setData后调用
        mChart.setVisibleXRangeMaximum(5);
        mChart.animateY(1500);
//        mChart.invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MsgEvent(MsgChart msg){
        LineDataSet set=new LineDataSet(msg.entries,"油耗");
        mChart.setData(new LineData(set));
        //数据显示设置
        set.setCircleRadius(2);
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setDrawFilled(true);
        set.setValueTextSize(12);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.fade_red);
            set.setFillDrawable(drawable);
        }
        mChart.invalidate();

    }

    static class getChartData implements Runnable {
        private WeakReference<Context> contextWeakReference;

        public getChartData(Context context) {
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public void run() {
            RecordDao dao = RecordDao.getInstance(contextWeakReference.get());
            List<Record> list = dao.queryButIsDelete("flag", Record.FLAG_OIL, "date", true);
            ArrayList<Entry> entries = new ArrayList<>();
            if (list != null) {
                for (Record record : list) {
                    long date = record.getDate();
                    String fuelC = record.getOil().getFuelC();
                    if (!TextUtils.isEmpty(fuelC)){
                        Entry entry = new Entry(date, Float.parseFloat(fuelC));
                        entries.add(entry);
                    }
                }
            }
            EventBus.getDefault().post(new MsgChart(entries));
        }
    }

}
