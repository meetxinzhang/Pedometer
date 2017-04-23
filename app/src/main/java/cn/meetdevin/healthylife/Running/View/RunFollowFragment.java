package cn.meetdevin.healthylife.Running.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.R;


/**
 * Created by XinZh on 2017/4/2.
 */

public class RunFollowFragment extends Fragment implements RunningActivity.OnRunningDataChangeListener{
    private static final String ARG_POSITION = "position";

    //View
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private TextView show_position;

    public static RunFollowFragment newInstance(int position) {
        RunFollowFragment f = new RunFollowFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //一定要在创建视图之前调用
        SDKInitializer.initialize(MyApplication.getContext());

        View rootView = inflater.inflate(R.layout.fragment_run_follow,container,false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        baiduMap = mapView.getMap();

        show_position = (TextView) rootView.findViewById(R.id.show_position);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRunningStepsChange(int steps, int minutes) {

    }

    @Override
    public void onRunningLocationChange(BDLocation bdLocation) {
        if(isFirstLocate){
            //定位到当前位置
            LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            //缩放级别设置为16,可选3-19
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
    }
}
