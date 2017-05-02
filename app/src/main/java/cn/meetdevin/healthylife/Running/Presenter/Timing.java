package cn.meetdevin.healthylife.Running.Presenter;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by XinZh on 2017/4/27.
 */

public class Timing extends Thread {
    private final String TAG = "Timing";
    private boolean isRunning = true;
    private int second = 0;
    private int minute = 0;
    private int hour = 0;

    public Timing(OnTimingChangeListener onTimingChangeListener){
        this.onTimingChangeListener = onTimingChangeListener;
    }
    @Override
    public void run() {
        while (isRunning){
            try {
                Log.d(TAG, "run: time "+ second +"s");
                Thread.sleep(1000);
                if(second < 59){
                    second++;
                }else {
                    if(minute < 59){
                        minute++;
                        second = 0;
                    }else {
                        hour++;
                        minute = 0;
                        second = 0;
                    }
                }

                onTimingChangeListener.onRunningSecondChange(hour,minute,second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSuspend(){
        isRunning = false;
    }
    public void onReStart(){
        isRunning = true;
        run();
    }

    public void requestUpDate(){
        onTimingChangeListener.onRunningSecondChange(hour,minute,second);
    }

    //自定义接口，向外传递服务内的即时信息
    OnTimingChangeListener onTimingChangeListener;
    public interface OnTimingChangeListener{
        void onRunningSecondChange(int hour,int minute,int second);
    }
}
