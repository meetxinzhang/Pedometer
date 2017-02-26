package cn.meetdevin.healthylife.Presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by XinZh on 2017/2/25.
 * 接受定时广播，启动pedometerService
 * 开机启动
 */

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,PedometerService.class);
        /**
         * 监测开机启动
         * 来自：http://stackoverflow.com/questions/31353411/app-doesnt-auto-start-an-app-when-booting-the-device-in-android
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.startService(i);
    }
}
