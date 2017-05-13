package cn.meetdevin.healthylife.Pedometer.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Presenter.StepsDataIntegration;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.mbarchartopenlib.DataMod;
import cn.meetdevin.mbarchartopenlib.MBarChartFrameLayout;



/**
 * Created by XinZh on 2017/3/29.
 */

public class StepsTrendFragment extends Fragment implements MBarChartFrameLayout.OnFocusItemChangeListener{
    private final String TAG = "TrendFragment";
    private static final int show_day = 3;
    private static final int show_startHour = 4;
    private static final String ARG_POSITION = "position";

    private int screenWidth;

    private List<DataMod> list;
    private MBarChartFrameLayout bar_chart_show_trend;
    private TextView show_date;
    private TextView show_steps;
    private TextView show_mintues;
    private MBarChartFrameLayout bar_chart_show_day;

    public static StepsTrendFragment newInstance(int position) {
        StepsTrendFragment f = new StepsTrendFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pedo_trend,container,false);
        bar_chart_show_trend = (MBarChartFrameLayout) rootView.findViewById(R.id.bar_chart_show_trend);
        show_date = (TextView) rootView.findViewById(R.id.pedo_trend_show_date);
        show_steps = (TextView) rootView.findViewById(R.id.pedo_trend_show_steps);
        show_mintues = (TextView) rootView.findViewById(R.id.pedo_trend_show_mintues);
        bar_chart_show_day = (MBarChartFrameLayout) rootView.findViewById(R.id.bar_chart_show_day);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        //获取屏幕宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        bar_chart_show_trend.setScreenSize(screenWidth);
        bar_chart_show_day.setScreenSize(screenWidth);

        list = StepsDataIntegration.getFormerData();
        bar_chart_show_trend.setData(list,show_day);
        bar_chart_show_trend.setOnFocusItemChangeListener(this);
        bar_chart_show_day.setOnFocusItemChangeListener(null);

//        bar_chart_show_day.requestUpdateFocusItem();
    }

    @Override
    public void onFucusItemChange(DataMod dataMod) {
        show_date.setText(dataMod.getYear()+"年"+dataMod.getMonth()+"月"+dataMod.getDay());
        show_steps.setText(dataMod.getVal()+"步");
        show_mintues.setText(dataMod.getMintues()+"分钟");
        bar_chart_show_day.setData(StepsDataIntegration.getTodayData(Integer.valueOf(dataMod.getYear()),Integer.valueOf(dataMod.getMonth()),Integer.valueOf(dataMod.getDay())),show_startHour);
    }
}
