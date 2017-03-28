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
import cn.meetdevin.healthylife.Pedometer.Dao.StepsDBHandler;
import cn.meetdevin.healthylife.Pedometer.Model.TodayStepsModel;
import cn.meetdevin.healthylife.R;

/**
 * Created by XinZh on 2017/3/28.
 */

public class FollowFragment extends Fragment implements PedometerActivity.OnActivityChangeListener{
    private final String TAG = "FollowFragment";
    private static final String ARG_POSITION = "position";
    final int STEPS_MESSAGE = 7;

    TodayStepsModel todayStepsModel;

    private TextView textView;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STEPS_MESSAGE:
                    textView.setText(msg.obj.toString());
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
        textView = (TextView) rootView.findViewById(R.id.textView);

        return rootView;
    }


    /**
     *  PedometerActivity.OnActivityChangeListener
     * @param steps
     */
    @Override
    public void onStepsChange(int steps) {
        Log.d(TAG, "onStepsChange: "+steps +"zg:"+todayStepsModel.getTodayTotalSteps());
        upDateSteps(steps + todayStepsModel.getTodayTotalSteps());
    }
    @Override
    public void onFinishStepsItem() {
        //从数据库获取更新
        StepsDBHandler stepsDBHandler = new StepsDBHandler();
        todayStepsModel = stepsDBHandler.getTodaySteps();
    }


    //更新UI
    private void upDateSteps(int newSteps){
//        Toast.makeText(this,couter,Toast.LENGTH_SHORT).show();
        Message msg = new Message();
        msg.what = STEPS_MESSAGE;
        msg.obj = newSteps;
        handler.sendMessage(msg);
        Log.d(TAG,Integer.toString(newSteps));
    }
}
