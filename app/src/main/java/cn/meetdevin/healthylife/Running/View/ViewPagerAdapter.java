package cn.meetdevin.healthylife.Running.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.meetdevin.healthylife.Pedometer.View.StepsFollowFragment;
import cn.meetdevin.healthylife.Pedometer.View.StepsRewardFragment;
import cn.meetdevin.healthylife.Pedometer.View.StepsTrendFragment;

/**
 * Created by XinZh on 2017/3/29.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT =3;
    private String titles[]={"跟踪","趋势","奖励"} ;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return RunFollowFragment.newInstance(position);
            case 1:
                return RunTrendFragment.newInstance(position);
            case 2:
                return RunRewardFragment.newInstance(position);
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
