package cn.meetdevin.healthylife.Presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import cn.meetdevin.healthylife.RegisterCallback;
import cn.meetdevin.healthylife.StepsChangeCallback;

/**
 * Remote Service
 * Created by XinZh on 2017/2/24.
 * 计步器后台服务
 */

public class PedometerService extends Service{
    public int counter = 0;//步数
    public RemoteCallbackList<StepsChangeCallback> callbackList = new RemoteCallbackList();//Android扩展类

    /**
     * 实现接口中暴露给客户端的Stub--Stub继承自Binder，它实现了IBinder接口
     * Remote Service 远程服务，独立于进程
     * 参考：http://www.jianshu.com/p/4a83becd758e
     * 和：http://www.linuxidc.com/Linux/2015-01/111148.htm
     * -------------
     * 这两个接口方法用于注册StepsChangeCallback对象
     * 参考：http://blog.csdn.net/goodlixueyong/article/details/50299963
     * 和http://www.race604.com/communicate-with-remote-service-3/
     */
    public RegisterCallback.Stub mBinder = new RegisterCallback.Stub() {
        @Override
        public void registerStepsChangeCallback(StepsChangeCallback callback) throws RemoteException {
            callbackList.register(callback);
        }
        @Override
        public void unRegisterStepsChangeCallback(StepsChangeCallback callback) throws RemoteException {
            callbackList.unregister(callback);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        //Stub其实就是IBinder的子类
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","onCreate");

        //发送定时广播唤醒自己
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000; //一小时毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        /**
         * 计步逻辑在这里实现
         * 其实不用开启线程，因为这是Remote Service，与UI不在同一个进程中，不存在阻塞问题
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter< 100){
                    try {
                        counter++;//模拟计步
                        Log.d("Service","steps: "+counter);
                        Thread.sleep(1000);
                        int len = callbackList.beginBroadcast();
                        for (int i =0;i< len;i++){
                            //回调给StepsActivity
                            callbackList.getBroadcastItem(i).onStepsChange(counter);
                        }
                        callbackList.finishBroadcast();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * 如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
         * 随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
         * 如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null
         */
        Log.d("Service","onStart");
        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消所有回调
        callbackList.kill();
        Log.d("Service","服务结束");
    }
}
