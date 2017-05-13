package cn.meetdevin.healthylife.Pedometer.Presenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDBHandler;
import cn.meetdevin.healthylife.Pedometer.Model.StepsItemModel;
import cn.meetdevin.mbarchartopenlib.DataMod;

/**
 * Created by XinZh on 2017/3/30.
 */

public class StepsDataIntegration {
    private static final int ignore = -1;

    public static List<DataMod>  getFormerData(){
        StepsDBHandler stepsDBHandler = new StepsDBHandler();
        List<StepsItemModel> formerSteps = stepsDBHandler.queryStepsData(ignore,ignore,ignore);
        return dataAndDateConformity(formerSteps);
    }


    public static List<DataMod> getTodayData(int year,int month,int day){
        List<DataMod> list = new ArrayList<>();

        if(year == ignore||month == ignore||day==ignore){
            Calendar calendar = Calendar.getInstance();
             year = calendar.get(Calendar.YEAR);
             month = calendar.get(Calendar.MONTH)+1;
             day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        StepsDBHandler stepsDBHandler = new StepsDBHandler();
        List<StepsItemModel> todaySteps = stepsDBHandler.queryStepsData(year,month,day);

        for (int h = 0;h<=23;h++){ //遍历小时
            int i;
            for (i=0;i<todaySteps.size();i++) { //遍历List
                StepsItemModel stepsItemModel = todaySteps.get(i);
                if(stepsItemModel.getStartHour() == h){
                    list.add(new DataMod(year,month,day,h,stepsItemModel.getMinutes(),stepsItemModel.getSteps()));
                    break;
                }
            }
            if(i == todaySteps.size()){
                list.add(new DataMod(year,month,day,h,0,0));
            }
        }
        return list;
    }


    private static List<DataMod> dataAndDateConformity(List<StepsItemModel> formerSteps){
        Calendar calendar = Calendar.getInstance();
        int yearOfNow = calendar.get(Calendar.YEAR);
        int monthOfNow = calendar.get(Calendar.MONTH)+1;
        int dayOfNow = calendar.get(Calendar.DAY_OF_MONTH);

        List<DataMod> list = new ArrayList<>();

        for (int y = yearOfNow; y>=2017; y--){ //遍历年

            for (int m = monthOfNow; m>=1; m--){ //遍历月

                for (int d = dayOfNow; d>=1; d--){ //遍历日
                    int i;
                    int steps = 0;
                    int minutes = 0;
                    for (i=0;i<formerSteps.size();i++){ //遍历 List
                        StepsItemModel stepsItemModel = formerSteps.get(i);
                        if(stepsItemModel.getYear() == y
                                && stepsItemModel.getMonth() == m
                                && stepsItemModel.getDay() == d){

                            steps += stepsItemModel.getSteps();
                            minutes += stepsItemModel.getMinutes();
                        }
                    }
                    list.add(new DataMod(y,m,d,ignore,minutes,steps));
                }
                monthOfNow --;
                dayOfNow = getDayCountOfMonth(yearOfNow,monthOfNow);
            }
            yearOfNow --;
            monthOfNow = 12;
        }
        return list;
    }

    private static int getDayCountOfMonth(int year,int month){
        boolean isRui = false;
        int dayCount = 0;

        if(year%100 == 0){
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
                break;
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
