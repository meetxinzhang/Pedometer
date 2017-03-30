package cn.meetdevin.healthylife.Pedometer.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import cn.meetdevin.healthylife.Pedometer.Presenter.PedometerService;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.RegisterCallback;
import cn.meetdevin.healthylife.StepsChangeCallback;

/**
 * 计步 View
 * 绑定远程服务获取数据
 * Created by XinZh on 2017/2/24.
 */

public class PedometerActivity extends FragmentActivity {
    private final String TAG = "PedometerActivity";

    //Properties
    boolean isBind = false; //是否和服务绑定

    //View
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    //Interface
    //服务控制客户端的回调接口
    private StepsChangeCallback callback = new StepsChangeCallback.Stub() {
        @Override
        public void onStepsChange(int steps) throws RemoteException {
            onActivityChangeListener.onStepsChange(steps);
        }
        @Override
        public void onFinishStepsItem() throws RemoteException {
            onActivityChangeListener.onFinishStepsItem();
        }
    };

    //客户端控制服务器的stub接口对象
    private RegisterCallback registerCallback;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //从连接中获取stub对象
            registerCallback = RegisterCallback.Stub.asInterface(service);
            try {
                //注册StepsChangeCallback对象
                registerCallback.registerStepsChangeCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //与服务意外断开连接,取消绑定时，这个方法是不会被调用的
            try {
                registerCallback.unRegisterStepsChangeCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            registerCallback = null;
        }
    };

    //自定义的接口，实时向 Fragment 传递步数
    OnActivityChangeListener onActivityChangeListener;

    public interface OnActivityChangeListener {
        //当步数改变时,通知外部更新UI
        void onStepsChange(int steps);
        //当计步状态改变时，通知外部是否存储
        void onFinishStepsItem();
    }
    public void setOnActivityChangeListener(
            OnActivityChangeListener onActivityChangeListener) {
        this.onActivityChangeListener = onActivityChangeListener;
    }


    //---------------------生命周期内方法---------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //绑定服务
        bindPedomaterService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当activity被回收时与服务解绑
        unBindPedomaterService();
    }

    //绑定远程服务
    private void bindPedomaterService() {
        Intent intent = new Intent(this, PedometerService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        isBind = true;
        Log.d(TAG, "bindPedomaterService");
    }

    //解绑远程服务
    private void unBindPedomaterService() {
        if (isBind) {
            unbindService(connection);
            try {
                registerCallback.unRegisterStepsChangeCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            registerCallback = null;
            Log.d(TAG, "unBindPedomaterService");
        }
        isBind = false;
    }

    //此activity的启动方法
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PedometerActivity.class);
        context.startActivity(intent);
    }

}
