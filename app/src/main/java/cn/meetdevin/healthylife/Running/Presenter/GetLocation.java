package cn.meetdevin.healthylife.Running.Presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Running.View.RunningActivity;

/**
 * Created by XinZh on 2017/4/22.
 */

public class GetLocation {
    private LocationClient locationClient;

    public GetLocation(RunningActivity runningActivity){
        locationClient = new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(new MyLocationListener());
        //setCV
        //权限判断
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(runningActivity, Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(runningActivity, Manifest.
                permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }//必须要Activity对象
        if(ContextCompat.checkSelfPermission(runningActivity, Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(runningActivity,permissions,1);
        }else {
            requestLocation();
        }
    }

    public void requestLocation(){
        initLocation();
        locationClient.start();
    }

    public void onActivityDestroy(){
        locationClient.stop();
    }


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        locationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).
                    append("\n");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).
                    append("\n");
            currentPosition.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //自定义接口
    OnPositionChangeListener onPositionChangeListener;
    public interface OnPositionChangeListener{
        void onPositionChange(float latitude,float longitude);
    }
    public void setOnPositionChangeListener(OnPositionChangeListener onPositionChangeListener){
        this.onPositionChangeListener = onPositionChangeListener;
    }
}
