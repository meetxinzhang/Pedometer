package cn.meetdevin.healthylife.Running.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.Running.Dao.RunningDataSP;
import cn.meetdevin.healthylife.Running.Presenter.RunningRewardAnalysis;


/**
 * Created by XinZh on 2017/4/2.
 */

public class RunRewardFragment extends Fragment{
    private static final String ARG_POSITION = "position";
    private TextView showRunCompleteCount;
    private TextView showRunTimeRecorder;
    private TextView showRecorderDate;


    public static RunRewardFragment newInstance(int position) {
        RunRewardFragment f = new RunRewardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_run_reward,container,false);
        showRunCompleteCount = (TextView) rootView.findViewById(R.id.show_run_complete_count);
        showRunTimeRecorder = (TextView) rootView.findViewById(R.id.show_run_time_recorder);
        showRecorderDate = (TextView) rootView.findViewById(R.id.show_run_recorder_date);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(RunningRewardAnalysis.getCompleteList().size()==0){
            showRunCompleteCount.setText("还未实现目标，继续努力！");
        }else {
            showRunCompleteCount.setText("总次数"+ RunningRewardAnalysis.getCompleteList().size());
        }

        int[] recorder = RunningDataSP.getRunningTimeRecorder();
        int hourRecorder = recorder[0];
        int minuteRecorder = recorder[1];
        int secondRecorder = recorder[2];
        showRunTimeRecorder.setText("持续时间："+hourRecorder+":"+minuteRecorder+":"+secondRecorder);
        int[] date = RunningDataSP.getRunningTimeRecorderDate();
        if(date[0]!=0&&date[1]!=0&&date[2]!=0){
            showRecorderDate.setText(date[0]+"年"+date[1]+"月"+date[2]+"日");
        }else {
            showRecorderDate.setText("无记录，继续努力！");
        }
    }
}
