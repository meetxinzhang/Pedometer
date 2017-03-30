package cn.meetdevin.mbarchartopenlib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by XinZh on 2017/3/30.
 */

public class DataSeparate {

    /**
     * 解离数据,这里是按年月份
     * @param oldList
     * @param month 月份，传入0 默认本月
     * @param year 年,传入0 默认本年
     * @return
     */
    public static List<DataMod> eparatesWithMonth(List<DataMod> oldList,int year,int month){
        List<DataMod> newList = new ArrayList<>();

        if(year == 0 && month == 0){
            for (int i=0;i<oldList.size();i++){
                DataMod dataMod = oldList.get(i);
                if( dataMod.getInfo1() == getThisYear() && dataMod.getInfo2() == getThisMonth()){
                    newList.add(dataMod);
                }
            }
        }
        return newList;
    }


    private static int getThisMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    private static int getThisYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
