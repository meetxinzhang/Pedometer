package cn.meetdevin.mbarchartopenlib;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XinZh on 2017/3/29.
 */

public class RViewAdapter extends RecyclerView.Adapter <RViewAdapter.ViewHolder> {
    private final String TAG = "RViewAdapter";
    private static final int show_day = 3;
    private static final int show_startHour = 4;

    private int screenWidth;

    private List<DataMod> list;
    private int flag;
    private int max;


    public RViewAdapter(List<DataMod> list,int flag,int screenWidth) {
        this.list = list;
        this.flag = flag;
        this.screenWidth = screenWidth;
        max = findMax();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_chart_item, parent, false);
        viewHolder = new ViewHolder(view, onRecyclerViewItemClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataMod dataMod = list.get(position);
        holder.progressBar.setMax(max);
        holder.progressBar.setProgress(dataMod.getVal());
        switch (flag){
            case show_day:
                if(dataMod.getDay()=="0"){
                    holder.textView.setText(" ");
                }else {
                    holder.textView.setText(String.valueOf(dataMod.getDay()));
                }

                break;
            case show_startHour:
                if(dataMod.getStartHour()==0){
                    holder.textView.setText(" ");
                }else {
                    holder.textView.setText(String.valueOf(dataMod.getStartHour()));
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar progressBar;
        TextView textView;
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public ViewHolder(View itemView, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar_val);
            textView = (TextView) itemView.findViewById(R.id.progressbar_info);
            textView.getLayoutParams().width = screenWidth/9;
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            DataMod dataMod = list.get(getPosition());
            onRecyclerViewItemClickListener.onRvItemClick(getPosition(),dataMod.getVal());
        }

    }

    //定义监听器外部实现接口，
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public static interface OnRecyclerViewItemClickListener {
        void onRvItemClick(int position, int val);
    }

    //模仿ListView的设置监听对象方法
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    //找到 list 中的最大值
    public int findMax(){
        int tempMax = 0;
        for (int i = 0;i<list.size();i++){
            DataMod dataMod = list.get(i);
            if(dataMod.getVal() >= tempMax){
                tempMax = dataMod.getVal();
            }
        }
        return tempMax;
    }
}