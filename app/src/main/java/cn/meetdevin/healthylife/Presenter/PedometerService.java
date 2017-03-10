package cn.meetdevin.healthylife.Presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import cn.meetdevin.healthylife.Dao.StepsDBHandler;
import cn.meetdevin.healthylife.Model.StepsItemModel;
import cn.meetdevin.healthylife.Model.TodayStepsModel;
import cn.meetdevin.healthylife.RegisterCallback;
import cn.meetdevin.healthylife.StepsChangeCallback;
import cn.meetdevin.healthylife.config.MyApplication;

/**
 * Remote Service
 * 计步器远程服务
 * 使用方法：
 * - 客户端与本服务绑定
 * - 通过 IPC 获取实时步数
 * -------------
 * Remote Service 远程服务，独立于进程
 * 参考：http://www.jianshu.com/p/4a83becd758e
 * 和：http://www.linuxidc.com/Linux/2015-01/111148.htm
 * -------------
 * Created by XinZh on 2017/2/24.
 */

public class PedometerService extends Service implements MyStepDcretor.OnSensorChangeListener {
    StepsItemModel stepsItemModel;
    TodayStepsModel todayStepsModel;
    long millis;

    StepsDBHandler stepsDBHandler;

    //Android AIDL 扩展类
    public RemoteCallbackList<StepsChangeCallback> callbackList = new RemoteCallbackList();

    SensorManager sensorManager;
    //自定义的加速度传感器监听
    MyStepDcretor myStepDcretor;

    /**
     * 实现接口：给客户端的Stub，Stub继承自Binder，它实现了IBinder接口
     * 两个方法，分别用于注册/反注册 StepsChangeCallback 接口给 RemoteCallbackList
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

//    //定义接口：用于服务重启时重设计步监听中的步数
//    public interface controlMyStepDcretor{
//        void reSetSteps(int newSteps);
//    }

    /**
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        //Stub其实就是IBinder的子类
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","onCreate");
        //初始化数据
        stepsDBHandler = new StepsDBHandler();
        stepsItemModel = recoveryData();
        todayStepsModel = stepsDBHandler.getTodaySteps();
        upDateUI();

        //发送定时广播唤醒自己
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 3*60*1000; //3分钟毫秒数
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
//               while (counter< 100){
//                    try {
//                        counter++;//模拟计步
//                        Log.d("Service","steps: "+counter);
//                        Thread.sleep(1000);
//                        int len = callbackList.beginBroadcast();
//                        for (int i =0;i< len;i++){
//                            //回调给StepsActivity
//                            callbackList.getBroadcastItem(i).onStepsChange(counter);
//                        }
//                        callbackList.finishBroadcast();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
                //注册加速度传感器和监听,并且从本地存储初始化步数
                myStepDcretor = new MyStepDcretor(0);

                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(myStepDcretor,sensor,SensorManager.SENSOR_DELAY_UI);

                myStepDcretor.setOnSensorChangeListener(PedometerService.this);
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
        //存储临时数据
        tempSave(stepsItemModel);
        Log.d("Service","服务结束");

        //发送定时广播唤醒自己
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int anHour = 60*1000; //一分钟
//        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
//        Intent intent1 = new Intent(this, AlarmReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    /**
     * 接口 MyStepDcretor.OnSensorChangeListener 中定义的方法
     */
    @Override
    public void onStepsListenerChange(int steps) {
        Log.d("Service","steps: "+steps);
        stepsItemModel.setSteps(steps);
        upDateUI();
    }

    @Override
    public void onPedometerStateChange(int pedometerState) {
        //Toast.makeText(MyApplication.getContext()," "+pedometerState,Toast.LENGTH_SHORT).show();
        Log.d("Service","onPedometerStateChange:"+pedometerState);
        if(pedometerState == 1){
            millis = System.currentTimeMillis();
        }
        if(pedometerState == 0){
            //存储一次计步数据
            int d = (int) ((System.currentTimeMillis() - millis)/1000/60);
            stepsItemModel.setMinutes(d);
            stepsDBHandler.insertToday(stepsItemModel);

            Log.d("Service","onPedometerStateChange: 记录了一次持续"+d+"分钟的运动");
            //Toast.makeText(MyApplication.getContext(),"记录了一次持续"+d+"分钟的运动",Toast.LENGTH_LONG).show();
        }
    }

//    /**
//     * 计步过程中每50步存储一次
//     * 感觉没必要
//     */
//    private void isSave(){
//        i++;
//        if(i==50){
//            //存储
//
//            i = 0;
//        }
//    }

    /**
     *  更新UI
     *  回调StepsChangeCallback.aidl 接口，向客户端(UI)传递数据
     */
    private void upDateUI(){
        int len = callbackList.beginBroadcast();
        //回调给所有绑定的客户端
        for (int i =0;i< len;i++){
            try {
                callbackList.getBroadcastItem(i).onStepsChange(stepsItemModel.getSteps());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

    /**
     * 临时数据存储
     */
    private void tempSave(StepsItemModel myStepsModel){
        SharedPreferences.Editor editor = getSharedPreferences("tempStepsData",MODE_PRIVATE).edit();
        editor.putInt("tempSteps",myStepsModel.getSteps());
        editor.putInt("tempMinutes",myStepsModel.getMinutes());
        editor.commit();
    }

    /**
     * 服务重启时恢复数据
     */
    private StepsItemModel recoveryData(){
        StepsItemModel myStepsModel;
        try {
            SharedPreferences pref = getSharedPreferences("tempStepsData", MODE_PRIVATE);
            myStepsModel = new StepsItemModel(
                    pref.getInt("tempSteps",0),
                    pref.getInt("tempMinutes",0));
            pref.edit().clear();//清除数据
            return myStepsModel;
        }catch (Exception e){
            Log.d("Service", "recoveryData: exception ");
            return new StepsItemModel(0,0);
        }
    }

}
