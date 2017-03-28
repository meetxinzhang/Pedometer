package cn.meetdevin.healthylife.Pedometer.Presenter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的加速度传感器监听
 * 在此处理加速度事件
 * 使用方法：
 * - 外部通过 setOnSensorChangeListener 方法传入回调对象
 * - 外部通过复写接口 OnSensorChangeListener 的 onChange() 方法获取步数
 * Created by XinZh on 2017/3/1.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class MyStepDcretor implements SensorEventListener {
    private final String TAG = "StepDcretor";


    //加速度，用x、y、z轴的三个加速度分量计算出
    public static float acceleration = 0;
    //当前传感器的值
    //float gravityNew = 0;
    //上次的加速度
    float lastAcceleration = 0;

    //是否上升的标志位
    boolean isDirectionUp = false;
    //持续上升次数
    int continueUpCount = 0;
    //上一点的持续上升的次数，为了记录波峰的上升次数
    int continueUpFormerCount = 0;
    //上一点的状态，上升还是下降
    boolean lastStatus = false;

    //波峰值
    float peakOfWave = 0;
    //波谷值
    float valleyOfWave = 0;
    //此次波峰的时间
    long timeOfThisPeak = 0;
    //上次波峰的时间
    long timeOfLastPeak = 0;
    //系统当前的时间
    long timeOfNow = 0;

    //初始阈(yu)值
    final float initialThreshold = (float) 1.7;
    // 动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    float threadThreshold = (float) 2.0;
    //用于存放计算阈值的波峰波谷差值
    final int valueNum = 5;
    float[] tempValue = new float[valueNum];
    int tempCount = 0;

    //初始范围
    float minValue = 11f;
    float maxValue = 19.6f;

    /**
     * 计步状态
     * 0-未计步   1-预备计步，计时中   2-正常计步中，存储
     */
    private int pedometerState = 0;
    //步数
    public static int CURRENT_SETP = 0;
    public static int TEMP_STEP = 0;
    private int lastStep = -1;

    private Timer timer;
    // 倒计时3.5秒，3.5秒内不会显示计步，用于屏蔽细微波动
    private long duration = 1500;
    private TimeCount timeCount;

    /**
     * 自定义的接口，实时向外传递步数
     */
    OnSensorChangeListener onSensorChangeListener;

    public interface OnSensorChangeListener {
        //当步数改变时,通知外部更新UI
        void onStepsListenerChange(int steps);
        //当计步状态改变时，通知外部是否存储
        void onPedometerStateChange(int pedometerState);
    }

    //获取接口对象
    public OnSensorChangeListener getOnSensorChangeListener() {
        return onSensorChangeListener;
    }

    //设置监听，传入回调对象
    public void setOnSensorChangeListener(
            OnSensorChangeListener onSensorChangeListener) {
        this.onSensorChangeListener = onSensorChangeListener;
    }


    //构造函数
    public MyStepDcretor(int newSteps) {
        super();
        CURRENT_SETP = newSteps;
    }

    /**
     * @param event
     * 加速度传感器
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                getAcceleration(event);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 计算加速度
     * 在波形图中，加速度在波峰波谷处改变较大，故获取到的加速度理论上是波峰波谷加速度
     * @param event
     */
    synchronized private void getAcceleration(SensorEvent event) {
        //忽略加速度方向，取绝对值
        acceleration = (float) Math.sqrt(Math.pow(event.values[0], 2)
                + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        detectorNewStep(acceleration);
    }

    /**
     * 侦测步子，并开始计步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     */
    public void detectorNewStep(float values) {
        if (lastAcceleration == 0) {
            lastAcceleration = values;
        } else {
            if (DetectorPeak(values, lastAcceleration)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();

                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= threadThreshold) && (timeOfNow - timeOfLastPeak) <= 2000) {
                    timeOfThisPeak = timeOfNow;
                    //视为一步，更新界面的处理，不涉及到算法
                    preStep();
                }
                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= initialThreshold)) {
                    timeOfThisPeak = timeOfNow;
                    threadThreshold = Peak_Valley_Thread(peakOfWave - valleyOfWave);
                }
            }
        }
        lastAcceleration = values;
    }

    /**
     * 检测波峰
     * 以下四个条件判断为波峰：
     *  1.目前点为下降的趋势：isDirectionUp为false
     *  2.之前的点为上升的趋势：lastStatus为true
     *  3.到波峰为止，持续上升大于等于2次
     *     - 这是因为：加速度传感器采集的频率比较高，一般大于30Hz
     *  4.波峰值大于1.2g,小于2g
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     * @param newValue 本次的加速度
     * @param oldValue 上次的加速度
     */
    public boolean DetectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {//可以换成差值大于某一值也可
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }

       // Log.v(TAG, "oldValue:" + oldValue);
        if (!isDirectionUp && lastStatus
                && (continueUpFormerCount >= 2 && (oldValue >= minValue && oldValue < maxValue))) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    /**
     * 阈值的计算
     * 1.通过波峰波谷的差值计算阈值
     * 2.记录4个值，存入tempValue[]数组中
     * 3.在将数组传入函数averageValue中计算阈值
     */
    public float Peak_Valley_Thread(float value) {
        float tempThread = threadThreshold;
        if (tempCount < valueNum) { //存储过程
            tempValue[tempCount] = value;
            tempCount++;
        } else { //计算过程
            tempThread = averageValue(tempValue, valueNum);
            for (int i = 1; i < valueNum; i++) { //   ？？？？？
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[valueNum - 1] = value;
        }
        return tempThread;

    }

    /**
     * 梯度化阈值
     * 1.计算数组的均值
     * 2.通过均值将阈值梯度化在一个范围里
     */
    public float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;
        if (ave >= 8) {
            Log.v(TAG, "超过8");
            ave = (float) 4.3;
        } else if (ave >= 7 && ave < 8) {
            Log.v(TAG, "7-8");
            ave = (float) 3.3;
        } else if (ave >= 4 && ave < 7) {
            Log.v(TAG, "4-7");
            ave = (float) 2.3;
        } else if (ave >= 3 && ave < 4) {
            Log.v(TAG, "3-4");
            ave = (float) 2.0;
        } else {
            Log.v(TAG, "else");
            ave = (float) 1.7;
        }
        return ave;
    }

    private void preStep() {
        if (pedometerState == 0) {
            // 开启计时器,duration 秒倒计时，每次间隔700毫秒
            timeCount = new TimeCount(duration, 700);
            timeCount.start();
            pedometerState = 1;
            //通知外部计步预备中
            onSensorChangeListener.onPedometerStateChange(pedometerState);
            Log.v(TAG, "开启计时器");
        } else if (pedometerState == 1) {
            TEMP_STEP++;
            Log.v(TAG, "预备计步中 TEMP_STEP:" + TEMP_STEP);
        } else if (pedometerState == 2) {
            CURRENT_SETP++;
            if (onSensorChangeListener != null) {
                //调用接口向外传递信息
                onSensorChangeListener.onStepsListenerChange(CURRENT_SETP);
            }
        }
    }

    /**
     * 自定义计时器类，复写onFinish()和onTick()方法
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            timeCount.cancel();
            CURRENT_SETP += TEMP_STEP;
            lastStep = -1;
//            CountTimeState = 2;
            Log.v(TAG, "计时正常结束");

            timer = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    if (lastStep == CURRENT_SETP) {
                        timer.cancel();
                        pedometerState = 0;
                        onSensorChangeListener.onPedometerStateChange(pedometerState);
                        lastStep = -1;
                        //-------------------------------
                        CURRENT_SETP = 0;
                        //-------------------------------
                        TEMP_STEP = 0;
                        Log.v(TAG, "停止计步：" + CURRENT_SETP);
                    } else {
                        lastStep = CURRENT_SETP;
                    }
                }
            };
            //0-调用后，多久开始一次次执行run()方法,1000*60*2-以后每次间隔多久调用run()
            timer.schedule(task, 0, 1000*60*2);
            pedometerState = 2;
            onSensorChangeListener.onPedometerStateChange(pedometerState);
        }

        //计时/预备计步过程，每700毫秒执行一次
        @Override
        public void onTick(long millisUntilFinished) {
            if (lastStep == TEMP_STEP) {
                Log.v(TAG, "onTick 计时停止");
                timeCount.cancel();
                pedometerState = 0;
                onSensorChangeListener.onPedometerStateChange(pedometerState);
                lastStep = -1;
                TEMP_STEP = 0;
            } else {
                lastStep = TEMP_STEP;
            }
        }

    }

}
