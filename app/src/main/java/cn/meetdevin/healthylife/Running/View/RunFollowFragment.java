package cn.meetdevin.healthylife.Running.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.R;


/**
 * Created by XinZh on 2017/4/2.
 */

public class RunFollowFragment extends Fragment{
    private static final String ARG_POSITION = "position";

    //View
    private MapView mapView;
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
        SDKInitializer.initialize(MyApplication.getContext());

        View rootView = inflater.inflate(R.layout.fragment_run_follow,container,false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
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
}
