package cn.meetdevin.healthylife.Running.Presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Running.View.RunningActivity;

/**
 * LocationClient 处理定位请求，这应该是个百度地图用来获取位置的后台服务
 * Created by XinZh on 2017/4/22.
 */

public class GetLocation {
    private LocationClient locationClient;

    public GetLocation(RunningActivity runningActivity){
        locationClient = new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(new MyLocationListener());

        setOnPositionChangeListener(runningActivity);//设置监听者
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
        initLocationOption();
        locationClient.start();
    }

    //当活动销毁时停止 locationClient
    public void onActivityDestroy(){
        locationClient.stop();
    }


    //配置 LocationClient 属性
    private void initLocationOption(){
        LocationClientOption option = new LocationClientOption();
        //设置刷新间隔
        option.setScanSpan(5000);
        //设置定位模式：当前选择高精度模式，会优先使用GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置不需要获取详细地址信息
        option.setIsNeedAddress(false);
        locationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation ) {

            //调用接口传递给外部
            onPositionChangeListener.onPositionChange(bdLocation);
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


    //自定义接口
    OnPositionChangeListener onPositionChangeListener;
    public interface OnPositionChangeListener{
        //当位置变化，向外部传递: 纬度，经度，经纬度字符串，位置描述字符串
        void onPositionChange(BDLocation bdLocation);
    }
    public void setOnPositionChangeListener(OnPositionChangeListener onPositionChangeListener){
        this.onPositionChangeListener = onPositionChangeListener;
    }
}
