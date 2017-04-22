package cn.meetdevin.healthylife.Pedometer.View;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDataSP;
import cn.meetdevin.healthylife.Pedometer.Presenter.RewardAnalysis;
import cn.meetdevin.healthylife.R;


/**
 * Created by XinZh on 2017/4/2.
 */

public class StepsRewardFragment extends Fragment implements PedometerActivity.OnActivityChangeListener{
    private static final String ARG_POSITION = "position";

    private TextView showCompleteCount;
    private TextView showRecorder;
    private TextView showRecorderDate;

    public static StepsRewardFragment newInstance(int position) {
        StepsRewardFragment f = new StepsRewardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pedo_reward,container,false);
        showCompleteCount = (TextView) rootView.findViewById(R.id.show_complete_count);
        showRecorder = (TextView) rootView.findViewById(R.id.show_recorder);
        showRecorderDate = (TextView) rootView.findViewById(R.id.show_recorder_date);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(RewardAnalysis.getCompleteList().size()==0){
            showCompleteCount.setText("还未实现目标，继续努力！");
        }else {
            showCompleteCount.setText("总次数"+RewardAnalysis.getCompleteList().size());
        }

        showRecorder.setText("步数："+StepsDataSP.getRecorder());
        int[] date = StepsDataSP.getRecorderDate();
        if(date[0]!=0&&date[1]!=0&&date[2]!=0){
            showRecorderDate.setText(date[0]+"年"+date[1]+"月"+date[2]+"日");
        }else {
            showRecorderDate.setText("无记录，继续努力！");
        }

    }

    @Override
    public void onStepsChange(int stepsOfThisTime, int stepsOfToady, int minutesOfToady, int lastRecorder) {
        showRecorder.setText(String.valueOf(lastRecorder));
        showRecorderDate.setText("今天|正在进行，加油！");
    }
    @Override
    public void onFinishStepsItem() {}
}
