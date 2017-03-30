package cn.meetdevin.mbarchartopenlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XinZh on 2017/3/29.
 */

public class MBarChartFrameLayout extends FrameLayout
        implements RViewAdapter.OnRecyclerViewItemClickListener{

    private final String TAG = "YourBarChartFrameLayout";
    RecyclerView recyclerView;
    TextView maxVal;
    TextView hafVal;

    RViewAdapter rViewAdapter;
    SnapHelper linearSnapHelper;
    Context context;

    public MBarChartFrameLayout(@NonNull Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.main_bar_chart,this);
        maxVal = (TextView) findViewById(R.id.maxVal);
        hafVal = (TextView) findViewById(R.id.hafVal);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //设置 item 居中
        linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
    }

    public void setData(List<DataMod> list){
        //解离数据
        rViewAdapter = new RViewAdapter(DataSeparate.eparatesWithMonth(list,0,0));

        rViewAdapter.setOnRecyclerViewItemClickListener(this);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        // 设置Adapter
        recyclerView.setAdapter(rViewAdapter);
        maxVal.setText(String.valueOf(rViewAdapter.findMax()));
        hafVal.setText(String.valueOf(rViewAdapter.findMax()/2));
    }

    @Override
    public void onRvItemClick(int position,int info,int val) {
        Log.d(TAG, "onRvItemClick: " + position);
        recyclerView.scrollToPosition(position);
    }
}
