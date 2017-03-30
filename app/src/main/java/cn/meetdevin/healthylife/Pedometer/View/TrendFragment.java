package cn.meetdevin.healthylife.Pedometer.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDBHandler;
import cn.meetdevin.healthylife.Pedometer.Model.TodayStepsModel;
import cn.meetdevin.healthylife.Pedometer.Presenter.TrendAnalysis;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.mbarchartopenlib.DataMod;
import cn.meetdevin.mbarchartopenlib.MBarChartFrameLayout;

/**
 * Created by XinZh on 2017/3/29.
 */

public class TrendFragment extends Fragment{
    private final String TAG = "TrendFragment";
    private static final String ARG_POSITION = "position";

    private MBarChartFrameLayout mBarChartFrameLayout;

    public static TrendFragment newInstance(int position) {
        TrendFragment f = new TrendFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trend,container,false);
        mBarChartFrameLayout = (MBarChartFrameLayout) rootView.findViewById(R.id.bar_chart);
        mBarChartFrameLayout.setData(TrendAnalysis.getFormerData());

        return rootView;
    }

}
