package cn.meetdevin.healthylife.Running.Presenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDBHandler;
import cn.meetdevin.healthylife.Pedometer.Model.StepsItemModel;
import cn.meetdevin.healthylife.Running.Dao.RunningDBHandler;
import cn.meetdevin.healthylife.Running.Model.RunningItemModel;
import cn.meetdevin.mbarchartopenlib.DataMod;

/**
 * Created by XinZh on 2017/3/30.
 */

public class RunningDataIntegration {
    private static final int ignore = -1;

    public static List<DataMod>  getFormerRunningData(){
        RunningDBHandler runningDBHandler = new RunningDBHandler();
        List<RunningItemModel> formerRunning = runningDBHandler.queryRunningData(ignore,ignore,ignore);
        return dataAndDateConformity(formerRunning);
    }


//    public static List<DataMod> getTodayRunningData(){
//        List<DataMod> list = new ArrayList<>();
//
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH)+1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        RunningDBHandler runningDBHandler = new RunningDBHandler();
//        List<RunningItemModel> todayRunning = runningDBHandler.queryRunningData(year,month,day);
//
//        for (int h = 0;h<=23;h++){ //遍历小时
//            int i;
//            for (i=0;i<todayRunning.size();i++) { //遍历List
//                RunningItemModel runningItemModel = todayRunning.get(i);
//                if(runningItemModel.getStartHour() == h){
//                    list.add(new DataMod(year,month,day,h,runningItemModel.getMinutes(),runningItemModel.getDistance()));
//                    break;
//                }
//            }
//            if(i == todayRunning.size()){
//                list.add(new DataMod(year,month,day,h,0,0));
//            }
//        }
//        return list;
//    }


    private static List<DataMod> dataAndDateConformity(List<RunningItemModel> formerRunning){
        Calendar calendar = Calendar.getInstance();
        int yearOfNow = calendar.get(Calendar.YEAR);
        int monthOfNow = calendar.get(Calendar.MONTH)+1;
        int dayOfNow = calendar.get(Calendar.DAY_OF_MONTH);

        List<DataMod> list = new ArrayList<>();

        for (int y = yearOfNow; y>=2017; y--){ //遍历年

            for (int m = monthOfNow; m>=1; m--){ //遍历月

                for (int d = dayOfNow; d>=1; d--){ //遍历日
                    int i;
                    int distance = 0;
                    int second = 0;
                    for (i=0;i<formerRunning.size();i++){ //遍历 List
                        RunningItemModel runningItemModel = formerRunning.get(i);
                        if(runningItemModel.getYear() == y
                                && runningItemModel.getMonth() == m
                                && runningItemModel.getDay() == d){

                            distance += runningItemModel.getDistance();
                            second += runningItemModel.getSecond();
                        }
                    }
                    list.add(new DataMod(y,m,d,ignore,distance,second/60));//条形图中按分钟为单位显示
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
