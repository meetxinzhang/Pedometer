package cn.meetdevin.healthylife.Running.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.meetdevin.healthylife.R;


/**
 * Created by XinZh on 2017/4/2.
 */

public class RunTrendFragment extends Fragment{
    private static final String ARG_POSITION = "position";

    public static RunTrendFragment newInstance(int position) {
        RunTrendFragment f = new RunTrendFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_run_trend,container,false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
