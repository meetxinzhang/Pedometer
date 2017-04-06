package cn.meetdevin.healthylife.Pedometer.View;

import android.content.Context;
import android.content.SharedPreferences;
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

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Pedometer.Dao.StepsDataSP;
import cn.meetdevin.healthylife.Pedometer.Presenter.DataIntegration;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.mbarchartopenlib.DataMod;
import cn.meetdevin.mbarchartopenlib.MBarChartFrameLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XinZh on 2017/3/28.
 */

public class FollowFragment extends Fragment implements PedometerActivity.OnActivityChangeListener{
    private final String TAG = "FollowFragment";
    private static final String ARG_POSITION = "position";
    private static final int show_startHour = 4;
    private final int STEPS_MESSAGE = 7;

    private List<DataMod> list;
    int stepsOfThisTime;
    int stepsOfToady;
    int minutesOfToady;

    private TextView showSteps;
    private TextView minutesSteps;
    private TextView goal_steps;
    private MBarChartFrameLayout simple_chart;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STEPS_MESSAGE:
                    showSteps.setText(String.valueOf(msg.arg2));
                    minutesSteps.setText(msg.obj.toString() + "分钟");
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
        minutesSteps = (TextView) rootView.findViewById(R.id.minutes_steps);
        goal_steps = (TextView) rootView.findViewById(R.id.goal_steps);
        simple_chart = (MBarChartFrameLayout) rootView.findViewById(R.id.simple_chart);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        onFinishStepsItem();
        goal_steps.setText("/"+StepsDataSP.getGoal());

        SharedPreferences spref = MyApplication.getContext().getSharedPreferences("tempFollowFragment", MODE_PRIVATE);
        stepsOfThisTime = spref.getInt("stepsOfThisTime",0);
        stepsOfToady = spref.getInt("stepsOfToady",0);
        minutesOfToady = spref.getInt("minutesOfToady",0);
        upDateSteps(stepsOfThisTime,stepsOfToady,minutesOfToady);
    }
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("tempFollowFragment", MODE_PRIVATE).edit();
        editor.putInt("stepsOfThisTime", stepsOfThisTime);
        editor.putInt("stepsOfToady", stepsOfToady);
        editor.putInt("minutesOfToady", minutesOfToady);
        editor.commit();
    }

    /**
     *  PedometerActivity.OnActivityChangeListener
     */
    @Override
    public void onStepsChange(int stepsOfThisTime,int stepsOfToady,int minutesOfToady,int lastRecorder) {
        upDateSteps(stepsOfThisTime,stepsOfToady,minutesOfToady);
    }


    @Override
    public void onFinishStepsItem() {
        //从数据库获取更新
        list = DataIntegration.getTodayData();
        simple_chart.setData(list,show_startHour);
    }


    //更新步数
    private void upDateSteps(int stepsOfThisTime,int stepsOfToady,int minutesOfToady){
//        Toast.makeText(this,couter,Toast.LENGTH_SHORT).show();
        Message msg = new Message();
        msg.what = STEPS_MESSAGE;
        msg.arg1 = stepsOfThisTime;
        msg.arg2 = stepsOfToady;
        msg.obj = minutesOfToady;
        handler.sendMessage(msg);

        this.stepsOfThisTime = stepsOfThisTime;
        this.stepsOfToady = stepsOfToady;
        this.minutesOfToady = minutesOfToady;
    }
}
