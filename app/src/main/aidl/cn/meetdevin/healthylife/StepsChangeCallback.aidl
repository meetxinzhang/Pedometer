// StepsChangeCallback.aidl
package cn.meetdevin.healthylife;

// Declare any non-default types here with import statements

interface StepsChangeCallback {
    /**
     *  当步数改变时，同过此方法通知UI
     *  在服务中回调activity
     */
    void onStepsChange(int steps);
}
