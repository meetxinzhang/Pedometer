package cn.meetdevin.healthylife.Pedometer.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.meetdevin.healthylife.Pedometer.Presenter.DataIntegration;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.mbarchartopenlib.MBarChartFrameLayout;

/**
 * Created by XinZh on 2017/3/29.
 */

public class StepsTrendFragment extends Fragment{
    private final String TAG = "TrendFragment";
    private static final int show_day = 3;
    private static final int show_startHour = 4;
    private static final String ARG_POSITION = "position";

    private MBarChartFrameLayout mBarChartFrameLayout;

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
        mBarChartFrameLayout = (MBarChartFrameLayout) rootView.findViewById(R.id.bar_chart);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBarChartFrameLayout.setData(DataIntegration.getFormerData(),show_day);
    }
}
