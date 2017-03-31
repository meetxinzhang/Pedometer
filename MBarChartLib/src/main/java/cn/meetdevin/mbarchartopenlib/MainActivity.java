package cn.meetdevin.mbarchartopenlib;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    MBarChartFrameLayout mBarChartFrameLayout;
    List<DataMod> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBarChartFrameLayout = (MBarChartFrameLayout) findViewById(R.id.test);
        list.add(new DataMod(2016,1,5,8,252));
        list.add(new DataMod(2016,1,16,8,2782));
        list.add(new DataMod(2017,1,26,9,2275));
        list.add(new DataMod(2017,1,30,10,42));
        list.add(new DataMod(2017,2,15,11,897));
        list.add(new DataMod(2017,2,20,12,4545));
        list.add(new DataMod(2017,3,10,12,55));
        list.add(new DataMod(2017,3,27,15,785));
        list.add(new DataMod(2017,3,28,16,785));
        list.add(new DataMod(2017,3,29,17,786));
        if(mBarChartFrameLayout == null){
            Log.d(TAG, "onCreate: null");
        }else {
            mBarChartFrameLayout.setData(list);
        }

    }
}
