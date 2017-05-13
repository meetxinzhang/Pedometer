package cn.meetdevin.mbarchartopenlib;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XinZh on 2017/3/29.
 */

public class MBarChartFrameLayout extends FrameLayout
        implements RViewAdapter.OnRecyclerViewItemClickListener{

    private final String TAG = "MBarChartFrameLayout";
    private static final int ignore = -1;
    private static final int month_unit = 30;
    private static final int day_unit = 1;
    private static final int week_unit = 7;

    private int lastFocusItemPosation = 5;

    private int screenWidth;
    private List<DataMod> list = new ArrayList<>();

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

        recyclerView.addOnScrollListener(new MyOnScrollListener());

        //设置 item 居中
        linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
    }

    public void setData(List<DataMod> list,int flag){
        this.list = list;
        //将数据传入适配器
        rViewAdapter = new RViewAdapter(list,flag,screenWidth);

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

    //必须在setData 之前调用
    public void setScreenSize(int screenWidth){
        this.screenWidth = screenWidth;
    }

    @Override
    public void onRvItemClick(int position,int val) {
        Log.d(TAG, "onRvItemClick: " + position);
        recyclerView.scrollToPosition(position);
    }


    private class MyOnScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState == RecyclerView.SCROLL_STATE_SETTLING){
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                if (recyclerView.get(firstItemPosition) instanceof DataMod) {
//                    int foodTypePosion = ((DataMod) recyclerView.get(firstItemPosition)).getFood_stc_posion();
//                    recyclerView.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
//                }

                    lastFocusItemPosation = (firstItemPosition+lastItemPosition-1)/2;
                    DataMod dm = list.get(lastFocusItemPosation);
                    System.out.println(dm.getYear()+"\n"+dm.getMonth()+"\n"+dm.getDay());
                    if(onFocusItemChangeListener!=null){
                        onFocusItemChangeListener.onFucusItemChange(dm);
                    }
                }
            }
        }
    }

    //自定义接口，向外传递左右滑动信息
    OnFocusItemChangeListener onFocusItemChangeListener;
    public interface OnFocusItemChangeListener {
        void onFucusItemChange(DataMod dm);
    }
    public void setOnFocusItemChangeListener(OnFocusItemChangeListener onFocusItemChangeListener) {
        this.onFocusItemChangeListener = onFocusItemChangeListener;
    }
    public void requestUpdateFocusItem(){
        DataMod dm = list.get(lastFocusItemPosation);
        if(onFocusItemChangeListener!=null){
            onFocusItemChangeListener.onFucusItemChange(dm);
        }
    }
}
