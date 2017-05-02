package cn.meetdevin.healthylife.Running.Presenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import cn.meetdevin.healthylife.MainActivity;
import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.Running.Dao.RunningDBHandler;
import cn.meetdevin.healthylife.Running.Dao.RunningDataSP;
import cn.meetdevin.healthylife.Running.Model.RunningItemModel;
import cn.meetdevin.healthylife.Running.View.RunningActivity;

/**
 * RunningService 前台服务
 * Created by XinZh on 2017/4/26.
 */

public class RunningService extends Service{
    private final String TAG = "RunningService";
    private boolean isRunning = false;
    private boolean isFirstStart = true;
    private boolean isComplete = false;
    private boolean isBreakRecorder = false;

    private RunningItemModel runningItemModel;
    private int second = 0;
    private int minute = 0;
    private int hour = 0;
    private int goalMinute;
    private int hourRecorder;
    private int minuteRecorder;
    private int secondRecorder;

    private RunningDBHandler runningDBHandler;

    private TimingControlBinder timeControlBinder = new TimingControlBinder();
    public class TimingControlBinder extends Binder{
        public void suspend(){
            isRunning = false;
        }
        public void reStart(){
            isRunning = true;
            if(isFirstStart){
                Calendar calendar = Calendar.getInstance();
                runningItemModel.setYear(calendar.get(Calendar.YEAR));
                runningItemModel.setMonth(calendar.get(Calendar.MONTH)+1);
                runningItemModel.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                isFirstStart = false;
            }
            startTiming();
        }
        public void requestUI(){
            Log.d(TAG, "requestUI: rrRrrr");
            onRunningServiceChangeListener.onRunningSecondChange(hour,minute,second);
        }
        public void setListener(OnRunningServiceChangeListener onRunningServiceChangeListener){
            setOnRunningServiceChangeListener(onRunningServiceChangeListener);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        //使用前台
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("healthylife")
                .setContentText("跑步中...")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);

        startTiming();
    }

    //计时模块
    private void startTiming(){
        new Thread(new Runnable() {
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
                        onRunningServiceChangeListener.onRunningSecondChange(hour,minute,second);

                        runningItemModel.setSecond((hour*60+minute)*60+second);
                        if(isComplete == false){
                            if(hour*60 + minute >= goalMinute){
                                runningItemModel.setIsCompelet(1);
                                isComplete = true;
                                sendNotify("跑步目标已完成！","持续时间："+hour+":"+minute+":"+second,"继续努力，注意休息");
                            }
                        }
                        if(isBreakRecorder==false){
                            if(hour>=hourRecorder){
                                if(minute>=minuteRecorder){
                                    if(second>=secondRecorder){
                                        isBreakRecorder = true;
                                        sendNotify("新的跑步记录！","持续时间："+hour+":"+minute+":"+second,"继续努力哦");
                                        RunningDataSP.setRunningTimeRecorder(hour,minute,second,
                                                runningItemModel.getYear(),runningItemModel.getMonth(),runningItemModel.getDay());
                                    }
                                }
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        runningItemModel = new RunningItemModel();
        return timeControlBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runningDBHandler = new RunningDBHandler();
        goalMinute = RunningDataSP.getRunningTimeGoal();
        int[] recorder = RunningDataSP.getRunningTimeRecorder();
        hourRecorder = recorder[0];
        minuteRecorder = recorder[1];
        secondRecorder = recorder[2];
        /*
         * 如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
         * 随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
         * 如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null
         */
        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        runningDBHandler.insertRunningData(runningItemModel);
    }

    private void sendNotify(String contentTitle, String contentText,String ticker){
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(contentTitle);//设置通知栏标题
        builder.setContentText(contentText);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());//通知产生的时间，系统获取到的时间
        builder.setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        builder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        builder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
        Intent intent = new Intent(this,RunningActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        manager.notify(1,builder.build());
    }

    //自定义接口，向外传递服务内的即时信息
    OnRunningServiceChangeListener onRunningServiceChangeListener;
    public interface OnRunningServiceChangeListener{
        void onRunningSecondChange(int hour,int minute,int second);
    }
    public void setOnRunningServiceChangeListener(OnRunningServiceChangeListener onRunningServiceChangeListener) {
        this.onRunningServiceChangeListener = onRunningServiceChangeListener;
    }
}
