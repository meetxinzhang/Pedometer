package cn.meetdevin.healthylife.Running.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import cn.meetdevin.healthylife.Pedometer.View.PedometerActivity;
import cn.meetdevin.healthylife.R;
import cn.meetdevin.healthylife.Running.Presenter.GetLocation;

/**
 * Created by XinZh on 2017/4/22.
 */

public class RunningActivity extends FragmentActivity{
    private final String TAG = "RunningActivity";

    //Properties
    boolean isBind = false; //是否和服务绑定
    GetLocation getLocation;

    //View
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocation = new GetLocation(this);//必须在setContentView()前
        setContentView(R.layout.activity_running);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_running);
        viewPager = (ViewPager) findViewById(R.id.viewPager_running);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLocation.onActivityDestroy();
    }

    /**
     * 对权限申请返回值的处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for (int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须赋予所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    getLocation.requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    //此activity的启动方法
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RunningActivity.class);
        context.startActivity(intent);
    }
}
