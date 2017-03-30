package cn.meetdevin.mbarchartopenlib;

import android.support.v7.widget.RecyclerView;
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
    private List<DataMod> list;
    private int max;


    public RViewAdapter(List<DataMod> list) {
        this.list = list;
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
        holder.textView.setText(String.valueOf(dataMod.getInfo3()));

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
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            DataMod dataMod = list.get(getPosition());
            onRecyclerViewItemClickListener.onRvItemClick(getPosition(),dataMod.getInfo1(),dataMod.getVal());
        }
    }

    //定义监听器外部实现接口，
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public static interface OnRecyclerViewItemClickListener {
        void onRvItemClick(int position,int info, int val);
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