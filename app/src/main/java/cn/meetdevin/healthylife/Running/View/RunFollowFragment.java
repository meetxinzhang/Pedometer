package cn.meetdevin.healthylife.Running.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.Running.Dao.RunningDataSP;
import cn.meetdevin.healthylife.Running.Presenter.GetLocation;
import cn.meetdevin.healthylife.Running.Presenter.RunningService;
import cn.meetdevin.healthylife.Running.Presenter.Timing;


/**
 * Created by XinZh on 2017/4/2.
 */

public class RunFollowFragment extends Fragment implements RunningActivity.OnRunningActivityChangeListener,
        Timing.OnTimingChangeListener,View.OnClickListener{
    private final String TAG = "RunFollowFragment";
    private static final String ARG_POSITION = "position";
    private final int TIME_MESSAGE = 7;
    private final int LOACTION_MESSAGE = 8;

    private boolean isFirstOnResume = true;
//    Timing timing = new Timing(this);

    RunningActivity runningActivity;

    //View
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private TextView show_position;

    private LinearLayout before_Run_view;
    private TextView show_set_run_time_goal;
    private Button reduce_time;
    private Button add_time;

    private LinearLayout when_startRun_view;
    private TextView show_running_time;
    private TextView show_running_time_goal;


    private Button start_or_suspend_run;
    private boolean isStartOrSuspend = true;//true-开始, false-暂停
    private LinearLayout restart_and_stop_run_view;
    private Button restart_run;
    private Button stop_run;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_MESSAGE:
                    show_running_time.setText(msg.arg1+":"+msg.arg2+":"+msg.obj+" ");
                    break;
                case LOACTION_MESSAGE:
                    show_position.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };


    public static RunFollowFragment newInstance(int position) {
        RunFollowFragment f = new RunFollowFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_run_follow,container,false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        show_position = (TextView) rootView.findViewById(R.id.show_position);

        before_Run_view = (LinearLayout) rootView.findViewById(R.id.before_Run_view);
        show_set_run_time_goal = (TextView) rootView.findViewById(R.id.show_set_run_time_goal);
        reduce_time = (Button) rootView.findViewById(R.id.reduce_time);
        add_time = (Button) rootView.findViewById(R.id.add_time);
        reduce_time.setOnClickListener(this);
        add_time.setOnClickListener(this);

        when_startRun_view = (LinearLayout) rootView.findViewById(R.id.when_startRun_view);
        show_running_time = (TextView) rootView.findViewById(R.id.show_running_time);
        show_running_time_goal = (TextView) rootView.findViewById(R.id.show_running_time_goal);


        start_or_suspend_run = (Button) rootView.findViewById(R.id.start_or_suspend_Run);
        restart_and_stop_run_view = (LinearLayout) rootView.findViewById(R.id.restart_and_stop_run_view);
        restart_run = (Button) rootView.findViewById(R.id.restart_run);
        stop_run = (Button) rootView.findViewById(R.id.stop_run);
        start_or_suspend_run.setOnClickListener(this);
        restart_run.setOnClickListener(this);
        stop_run.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        runningActivity = (RunningActivity) getActivity();
        runningActivity.setOnRunningActivityChangeListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isStartOrSuspend == true){
            before_Run_view.setVisibility(View.VISIBLE);
            when_startRun_view.setVisibility(View.GONE);
        }else {
            before_Run_view.setVisibility(View.GONE);
            when_startRun_view.setVisibility(View.VISIBLE);
            start_or_suspend_run.setText("暂停");
        }
        show_set_run_time_goal.setText(RunningDataSP.getRunningTimeGoal()+" 分钟");
        show_running_time_goal.setText(RunningDataSP.getRunningTimeGoal()+" 分钟");
        if(isFirstOnResume==false){
            runningActivity.timingControlBinder.requestUI();
        }
        isFirstOnResume = false;
        Log.d(TAG, "onResume: ");
//        timing.requestUpDate();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRunningLocationChange(BDLocation bdLocation) {
        if(isFirstLocate){
            //定位到当前位置
            LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            //缩放级别设置为16,可选3-19
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);

            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).
                    append("\n");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).
                    append("\n");
            currentPosition.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }

            upDateLocationUI(currentPosition.toString());
            isFirstLocate = false;
        }
    }

    @Override
    public void onRunningSecondChange(int hour,int minute,int second) {
        upDateTimeUI(hour,minute,second);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reduce_time:
//                Toast.makeText(runningActivity,"-",Toast.LENGTH_SHORT).show();
                if(RunningDataSP.getRunningTimeGoal()>1){
                    if(RunningDataSP.getRunningTimeGoal()<=5){
                        RunningDataSP.setRunningTimeGoal(RunningDataSP.getRunningTimeGoal() - 1 );
                        show_set_run_time_goal.setText(RunningDataSP.getRunningTimeGoal()+" 分钟");
                    }else {
                        RunningDataSP.setRunningTimeGoal(RunningDataSP.getRunningTimeGoal() - 5 );
                        show_set_run_time_goal.setText(RunningDataSP.getRunningTimeGoal()+" 分钟");
                    }
                }
                break;
            case R.id.add_time:
//                Toast.makeText(runningActivity,"+",Toast.LENGTH_SHORT).show();
                if(RunningDataSP.getRunningTimeGoal()>=5){
                    RunningDataSP.setRunningTimeGoal(RunningDataSP.getRunningTimeGoal() + 5 );
                    show_set_run_time_goal.setText(RunningDataSP.getRunningTimeGoal()+" 分钟");
                }else {
                    RunningDataSP.setRunningTimeGoal(RunningDataSP.getRunningTimeGoal() + 1 );
                    show_set_run_time_goal.setText(RunningDataSP.getRunningTimeGoal()+" 分钟");
                }
                break;
            case R.id.start_or_suspend_Run:
                if(isStartOrSuspend){
//                    runningActivity.timingControlBinder.setListener(runningActivity);
                    runningActivity.timingControlBinder.reStart();
//                    timing.start();
                    isStartOrSuspend = false;
                    start_or_suspend_run.setText("暂停");
                    //地图上方的界面
                    before_Run_view.setVisibility(View.GONE);
                    when_startRun_view.setVisibility(View.VISIBLE);
                }else {
                    runningActivity.timingControlBinder.suspend();
//                    timing.onSuspend();
                    start_or_suspend_run.setVisibility(View.GONE);
                    restart_and_stop_run_view.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.restart_run:
                runningActivity.timingControlBinder.reStart();
//                timing.onReStart();
                restart_and_stop_run_view.setVisibility(View.GONE);
                start_or_suspend_run.setVisibility(View.VISIBLE);
                isStartOrSuspend = false;
                start_or_suspend_run.setText("暂停");
                break;
            case R.id.stop_run:
                //停止跑步服务
                runningActivity.timingControlBinder.stopSer();
//                runningActivity.unBindRunningService();
//                timing.onSuspend();
                isStartOrSuspend = true;
                start_or_suspend_run.setText("开始");
                start_or_suspend_run.setVisibility(View.VISIBLE);
                restart_and_stop_run_view.setVisibility(View.GONE);
                before_Run_view.setVisibility(View.VISIBLE);
                when_startRun_view.setVisibility(View.GONE);
                break;
        }
    }

    private void upDateTimeUI(int hour,int minute,int second){
        Message msg = new Message();
        msg.what = TIME_MESSAGE;
        msg.arg1 = hour;
        msg.arg2 = minute;
        msg.obj = second;
        handler.sendMessage(msg);
    }
    private void upDateLocationUI(String location){
        Message msg = new Message();
        msg.what = LOACTION_MESSAGE;
        msg.obj = location;
        handler.sendMessage(msg);
    }
}
