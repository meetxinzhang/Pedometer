package cn.meetdevin.mbarchartopenlib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XinZh on 2017/3/30.
 */

public class StatisticalData {
    private static final int ignore = -1;

    private static final int month_unit = 30;
    private static final int day_unit = 1;
    private static final int week_unit = 7;


    /**
     * 统计数据
     * @param oldList
     * @param unit 统计单位,month-月,week-周,day-日
     * @return
     */
    public static List<DataMod> statisticalDataWithUnit(List<DataMod> oldList,int unit){
        switch (unit){
            case day_unit:
                return withDay(oldList);
            case month_unit:
                return withMonth(oldList);
            case week_unit:
                return withWeek(oldList);
            default:
                return  new ArrayList<>();
        }

    }

    private static List<DataMod> withDay(List<DataMod> oldList){
        List<DataMod> newlist = new ArrayList<>();

        for (int i=0;i<oldList.size();i++){
            DataMod dataMod = oldList.get(i);
            newlist.add(dataMod);
//            if(dataMod.getDay().equals("1")){
//                dataMod.setDay(dataMod.getMonth()+"/1");
//            }
        }

        return newlist;
    }

    private static List<DataMod> withMonth(List<DataMod> oldList){

        return oldList;
    }

    private static List<DataMod> withWeek(List<DataMod> oldList){

        return oldList;
    }


}
