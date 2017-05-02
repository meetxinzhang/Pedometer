package cn.meetdevin.healthylife.Running.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;

import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.Running.Presenter.GetLocation;
import cn.meetdevin.healthylife.Running.Presenter.RunningService;

/**
 * Created by XinZh on 2017/4/22.
 */

public class RunningActivity extends FragmentActivity implements RunningService.OnRunningServiceChangeListener
        ,GetLocation.OnPositionChangeListener{
    private final String TAG = "RunningActivity";

    //Properties
    private boolean isBind = false; //是否和服务绑定
//    GetLocation getLocation;

    public RunningService.TimingControlBinder timingControlBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            timingControlBinder = (RunningService.TimingControlBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    //View
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定服务
        bindRunningService();
        if(timingControlBinder==null){
            Log.d(TAG, "onCreate: timingControlBinder==null");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /*
                        设置 RunningService.OnRunningServiceChangeListener 监听者
                        在bindservice后马上调用 service 做事情，这样会返回空指针，
                        因为bindservice是异步操作，没有办法马上绑定服务就可以用。
                         */
                        while (timingControlBinder==null){
                            Thread.sleep(500);
                        }
                        timingControlBinder.setListener(RunningActivity.this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

//        getLocation = new GetLocation(this);//必须在setContentView()前
        setContentView(R.layout.activity_running);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_running);
        viewPager = (ViewPager) findViewById(R.id.viewPager_running);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getLocation.onActivityDestroy();
    }

    /**
     * 运行时 权限申请返回值的处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for (int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须赋予所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
//                    getLocation.requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public void bindRunningService(){
        Intent intent = new Intent(this, RunningService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        isBind = true;
        Log.d(TAG, "bindRunningService");
    }
    public void unBindRunningService(){
        unbindService(connection);
        isBind = false;
    }



    /*
     实现接口 GetLocation.OnPositionChangeListener 中定义的方法
     调用接口传递即时位置信息给 fragment
     */
    @Override
    public void onPositionChange(BDLocation bdLocation) {
        onRunningActivityChangeListener.onRunningLocationChange(bdLocation);
    }

    @Override
    public void onRunningSecondChange(int hour, int minute, int second) {
        onRunningActivityChangeListener.onRunningSecondChange(hour,minute,second);
    }


    //自定义接口，给 Fragment 传递即时信息
    OnRunningActivityChangeListener onRunningActivityChangeListener;
    public interface OnRunningActivityChangeListener {
        void onRunningLocationChange(BDLocation bdLocation);
        void onRunningSecondChange(int hour,int minute,int second);
    }
    public void setOnRunningActivityChangeListener(OnRunningActivityChangeListener onRunningActivityChangeListener){
        this.onRunningActivityChangeListener = onRunningActivityChangeListener;
    }


    //此 activity 的启动方法
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RunningActivity.class);
        context.startActivity(intent);
    }


}
