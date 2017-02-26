// IPedometerService.aidl
package cn.meetdevin.healthylife;

import cn.meetdevin.healthylife.StepsChangeCallback;

// Declare any non-default types here with import statements

interface RegisterCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     * 注册StepsChangeCallback，在activity中将StepsChangeCallback对象传给Service
     */
    void registerStepsChangeCallback(StepsChangeCallback callback);
    void unRegisterStepsChangeCallback(StepsChangeCallback callback);
}
