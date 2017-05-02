package cn.meetdevin.healthylife.Running.Presenter;

import java.util.ArrayList;
import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDataSP;
import cn.meetdevin.healthylife.Pedometer.Presenter.StepsDataIntegration;
import cn.meetdevin.healthylife.Running.Dao.RunningDataSP;
import cn.meetdevin.mbarchartopenlib.DataMod;

/**
 * Created by XinZh on 2017/4/2.
 */

public class RunningRewardAnalysis {

    public static List<DataMod> getCompleteList(){
        List<DataMod> newList = new ArrayList<>();
        List<DataMod> oldList = RunningDataIntegration.getFormerRunningData();
        for (int i=0;i<oldList.size();i++){
            DataMod dataMod = oldList.get(i);
            if(dataMod.getVal()==1){//对应 RunningItemModel 的最后一项属性 isCompelet // 0-未完成目标，1-完成目标
                newList.add(dataMod);
            }
        }
        return newList;
    }

}
