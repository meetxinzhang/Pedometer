package cn.meetdevin.healthylife.Pedometer.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import cn.meetdevin.healthylife.Pedometer.Dao.StepsDataSP;
import cn.meetdevin.healthylife.Pedometer.Presenter.DataIntegration;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.mbarchartopenlib.DataMod;
import cn.meetdevin.mbarchartopenlib.MBarChartFrameLayout;

/**
 * Created by XinZh on 2017/3/28.
 */

public class FollowFragment extends Fragment implements PedometerActivity.OnActivityChangeListener{
    private final String TAG = "FollowFragment";
    private static final String ARG_POSITION = "position";
    private static final int show_startHour = 4;
    private final int STEPS_MESSAGE = 7;

    private List<DataMod> list;
    private int formerTotalSteps;
    private int formerTotalMinutes;
    private int lastRecorder;//上次的最高记录

    private TextView goalSteps;
    private TextView showSteps;
    private TextView minutesSteps;
    private MBarChartFrameLayout simple_chart;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STEPS_MESSAGE:
                    showSteps.setText(msg.obj.toString());
            }
        }
    };


    public static FollowFragment newInstance(int position) {
        FollowFragment f = new FollowFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PedometerActivity pedometerActivity = (PedometerActivity) getActivity();
        pedometerActivity.setOnActivityChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_follow,container,false);
        showSteps = (TextView) rootView.findViewById(R.id.show_steps);
        goalSteps = (TextView) rootView.findViewById(R.id.goal_steps);
        minutesSteps = (TextView) rootView.findViewById(R.id.minutes_steps);
        simple_chart = (MBarChartFrameLayout) rootView.findViewById(R.id.simple_chart);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        onFinishStepsItem();
        upDateSteps(formerTotalSteps+StepsDataSP.tempRecoverySteps().getSteps());

    }


    /**
     *  PedometerActivity.OnActivityChangeListener
     * @param steps
     */
    @Override
    public void onStepsChange(int steps) {
        Log.d(TAG, "onStepsChange: "+steps +"zg:"+formerTotalSteps);
        upDateSteps(steps + formerTotalSteps);
        if(steps + formerTotalSteps >= lastRecorder){
            Calendar calendar = Calendar.getInstance();
            StepsDataSP.setRecorder(steps + formerTotalSteps,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
    }


    @Override
    public void onFinishStepsItem() {
        //从数据库获取更新
        formerTotalMinutes = 0;
        formerTotalSteps = 0;
        list = DataIntegration.getTodayData();
        for (int i=0;i<list.size();i++){
            formerTotalSteps += list.get(i).getVal();
            formerTotalMinutes += list.get(i).getMintues();
        }
        if(formerTotalMinutes==0){
            minutesSteps.setText("少于1分钟");
        }else {
            minutesSteps.setText(String.valueOf(formerTotalMinutes)+" 分钟");
        }
        simple_chart.setData(list,show_startHour);
    }


    //更新步数
    private void upDateSteps(int newSteps){
//        Toast.makeText(this,couter,Toast.LENGTH_SHORT).show();
        Message msg = new Message();
        msg.what = STEPS_MESSAGE;
        msg.obj = newSteps;
        handler.sendMessage(msg);
        Log.d(TAG,Integer.toString(newSteps));
    }
}
