package cn.meetdevin.healthylife.Pedometer.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.meetdevin.healthylife.R;

/**
 * Created by XinZh on 2017/3/29.
 */

public class TrendFragment extends Fragment{
    private final String TAG = "TrendFragment";
    private static final String ARG_POSITION = "position";

    public static TrendFragment newInstance(int position) {
        TrendFragment f = new TrendFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trend,container,false);

        return rootView;
    }
}
