// StepsChangeCallback.aidl
package cn.meetdevin.healthylife;

// Declare any non-default types here with import statements

interface StepsChangeCallback {

    //当步数改变时，通过此方法通知UI
    void onStepsChange(int stepsOfThisTime,int stepsOfToady,int minutesOfToady,int lastRecorder);

    //当完成一次计步过程
    void onFinishStepsItem();
}
