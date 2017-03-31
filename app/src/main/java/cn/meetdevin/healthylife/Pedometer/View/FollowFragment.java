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

import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Presenter.DataIntegration;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.mbarchartopenlib.DataMod;

/**
 * Created by XinZh on 2017/3/28.
 */

public class FollowFragment extends Fragment implements PedometerActivity.OnActivityChangeListener{
    private final String TAG = "FollowFragment";
    private static final String ARG_POSITION = "position";
    private final int STEPS_MESSAGE = 7;

    private List<DataMod> list;
    private int formerTotalSteps;

    private TextView showSteps;
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

        return rootView;
    }


    /**
     *  PedometerActivity.OnActivityChangeListener
     * @param steps
     */
    @Override
    public void onStepsChange(int steps) {
        Log.d(TAG, "onStepsChange: "+steps +"zg:"+formerTotalSteps);
        upDateSteps(steps + formerTotalSteps);
    }
    @Override
    public void onFinishStepsItem() {
        //从数据库获取更新
        list = DataIntegration.getTodayData();
        for (int i=0;i<list.size();i++){
            formerTotalSteps += list.get(i).getVal();
        }
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
