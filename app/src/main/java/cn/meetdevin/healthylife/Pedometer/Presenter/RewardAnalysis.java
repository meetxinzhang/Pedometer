package cn.meetdevin.healthylife.Pedometer.Presenter;

import java.util.ArrayList;
import java.util.List;
import cn.meetdevin.healthylife.Pedometer.Dao.StepsDataSP;
import cn.meetdevin.mbarchartopenlib.DataMod;

/**
 * Created by XinZh on 2017/4/2.
 */

public class RewardAnalysis {

    public static List<DataMod> getCompleteList(){
        List<DataMod> newList = new ArrayList<>();
        List<DataMod> oldList = DataIntegration.getFormerData();
        for (int i=0;i<oldList.size();i++){
            DataMod dataMod = oldList.get(i);
            if(dataMod.getVal() >= StepsDataSP.getGoal()){
                newList.add(dataMod);
            }
        }
        return newList;
    }

    public static int findTheMostTimes(){

        return 0;
    }
}
