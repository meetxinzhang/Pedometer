package cn.meetdevin.healthylife.View;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import cn.meetdevin.healthylife.Presenter.PedometerService;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.RegisterCallback;
import cn.meetdevin.healthylife.StepsChangeCallback;

/**
 * 计步 View
 * 绑定远程服务获取数据
 * Created by XinZh on 2017/2/24.
 */

public class StepsActivity extends Activity {
    //View
    TextView showSteps_t;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STEPS_MESSAGE:
                    showSteps_t.setText(msg.obj.toString());
            }
        }
    };

    //Properties
    final int STEPS_MESSAGE = 7;
    int couter;//步数
    boolean isBind = false; //是否和服务绑定

    //Interface
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

    //服务回调接口
    private StepsChangeCallback callback = new StepsChangeCallback.Stub() {
        @Override
        public void onStepsChange(int steps) throws RemoteException {
            couter = steps;
            upDateData();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        showSteps_t = (TextView) findViewById(R.id.showSteps_t);

        //绑定服务
        bindPedomaterService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当activity被回收时与服务解绑
        unBindPedomaterService();
    }

    //更新UI
    private void upDateData(){
//        Toast.makeText(this,couter,Toast.LENGTH_SHORT).show();
        Message msg = new Message();
        msg.what = STEPS_MESSAGE;
        msg.obj = couter;
        handler.sendMessage(msg);
        Log.d("StepsActivity",Integer.toString(couter));
    }


    //绑定远程服务
    private void bindPedomaterService() {
        Intent intent = new Intent(this, PedometerService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        isBind = true;
        Log.d("StepsActivity", "绑定服务");
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
            Log.d("StepsActivity", "解绑服务");
        }
        isBind = false;
    }

    //此activity的启动方法
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, StepsActivity.class);
        context.startActivity(intent);
    }

}
