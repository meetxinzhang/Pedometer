package cn.meetdevin.healthylife.Presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.meetdevin.healthylife.Dao.StepsDBHandler;

/**
 * Created by XinZh on 2017/2/25.
 * 接受定时广播，启动pedometerService
 * 开机启动
 */

public class AlarmReceiver extends BroadcastReceiver{
    private final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_DATE_CHANGED)){
            //当日期改变时
            StepsDBHandler stepsDBHandler = new StepsDBHandler();
            stepsDBHandler.insertFormerTodays();
        }else {
            Intent i = new Intent(context,PedometerService.class);
            /**
             * 监测开机启动
             * 来自：http://stackoverflow.com/questions/31353411/app-doesnt-auto-start-an-app-when-booting-the-device-in-android
             */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startService(i);
            Log.d(TAG, "onReceive: startService");
        }

    }
}
