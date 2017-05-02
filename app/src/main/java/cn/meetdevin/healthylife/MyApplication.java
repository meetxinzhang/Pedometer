package cn.meetdevin.healthylife;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by XinZh on 2017/3/9.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //一定要在创建视图之前调用
        SDKInitializer.initialize(this);

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
