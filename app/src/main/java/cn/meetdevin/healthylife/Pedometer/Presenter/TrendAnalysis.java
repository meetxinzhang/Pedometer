package cn.meetdevin.healthylife.Pedometer.Presenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDBHandler;
import cn.meetdevin.healthylife.Pedometer.Model.TodayStepsModel;
import cn.meetdevin.mbarchartopenlib.DataMod;

/**
 * Created by XinZh on 2017/3/30.
 */

public class TrendAnalysis {

    public static List<DataMod>  getFormerData(){
        StepsDBHandler stepsDBHandler = new StepsDBHandler();
        List<TodayStepsModel> formerSteps = stepsDBHandler.getFormerSteps();
        return dataConformity(formerSteps);
    }

    private static List<DataMod> dataConformity(List<TodayStepsModel> formerSteps){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        List<DataMod> list = new ArrayList<>();

        for (int y=year;y>=2017;y--){ //遍历年
            if(y != year){
                month = 12;
            }
            for (int m=month;m>=1;m--){ //遍历月
                if(y!= year||m != month){
                    day = getDayCountOfMonth(y,m)+1;
                }
                for (int d=day-1;d>=1;d--){ //遍历日
                    int i;
                    for (i=0;i<formerSteps.size();i++){ //遍历List
                        TodayStepsModel todayStepsModel = formerSteps.get(i);
                        if(todayStepsModel.getMyDate()[0] == y
                                && todayStepsModel.getMyDate()[1] == m
                                && todayStepsModel.getMyDate()[2] == d){
                            list.add(new DataMod(todayStepsModel.getMyDate()[0],
                                    todayStepsModel.getMyDate()[1],
                                    todayStepsModel.getMyDate()[2],todayStepsModel.getTodayTotalSteps()));
                            break;
                        }else if (i == formerSteps.size()-1){
                            list.add(new DataMod(y,m,d,0));
                        }
                    }
                }
            }
        }
        return list;
    }

    private static int getDayCountOfMonth(int year,int month){
        boolean isRui = false;
        int dayCount = 0;

        if(year%100 ==0){
            if(year%400 == 0){
                isRui = true;
            }
        }else if(year%4 == 0){
            isRui = true;
        }else {
            isRui = false;
        }

        switch (month){
            case 1:dayCount = 31;break;
            case 2:
                if(isRui == true){
                    dayCount = 29;
                }else {
                    dayCount = 28;
                }
            case 3:dayCount = 31;break;
            case 4:dayCount = 30;break;
            case 5:dayCount = 31;break;
            case 6:dayCount = 30;break;
            case 7:dayCount = 31;break;
            case 8:dayCount = 31;break;
            case 9:dayCount = 30;break;
            case 10:dayCount = 31;break;
            case 11:dayCount = 30;break;
            case 12:dayCount = 31;break;
        }
        return dayCount;
    }
}
