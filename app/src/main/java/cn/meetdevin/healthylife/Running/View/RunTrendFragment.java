package cn.meetdevin.healthylife.Running.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.Running.Presenter.RunningDataIntegration;
import cn.meetdevin.mbarchartopenlib.MBarChartFrameLayout;


/**
 * Created by XinZh on 2017/4/2.
 */

public class RunTrendFragment extends Fragment{
    private static final String ARG_POSITION = "position";
    private static final int show_day = 3;
    private static final int show_startHour = 4;
    private MBarChartFrameLayout mBarChartFrameLayout;

    public static RunTrendFragment newInstance(int position) {
        RunTrendFragment f = new RunTrendFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_run_trend,container,false);
        mBarChartFrameLayout = (MBarChartFrameLayout) rootView.findViewById(R.id.running_bar_chart);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBarChartFrameLayout.setData(RunningDataIntegration.getFormerRunningData(),show_day);
    }
}
