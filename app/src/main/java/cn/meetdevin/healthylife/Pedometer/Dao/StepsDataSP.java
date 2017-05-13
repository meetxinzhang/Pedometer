package cn.meetdevin.healthylife.Pedometer.Dao;

import android.content.SharedPreferences;


import java.util.Calendar;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Pedometer.Model.StepsItemModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XinZh on 2017/3/31.
 */

public class StepsDataSP {

        public static void tempSaveSteps(StepsItemModel stepsItemModel) {
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("tempStepsData", MODE_PRIVATE).edit();
            editor.putInt("tempYear", stepsItemModel.getYear());
            editor.putInt("tempMonth", stepsItemModel.getMonth());
            editor.putInt("tempDay", stepsItemModel.getDay());
            editor.putInt("tempStartHour", stepsItemModel.getStartHour());
            editor.putInt("tempMinutes", stepsItemModel.getMinutes());
            editor.putInt("tempSteps", stepsItemModel.getSteps());

            editor.commit();
        }

        public static StepsItemModel tempRecoverySteps() {
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("tempStepsData", MODE_PRIVATE);
            Calendar calendar = Calendar.getInstance();

            int year = pref.getInt("tempYear",0);
            int month = pref.getInt("tempMonth",0);
            int day = pref.getInt("tempDay",0);

            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH)+1;
            int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

            StepsItemModel stepsItemModel;
            if(year == yearNow && month == monthNow && day == dayNow){
                stepsItemModel = new StepsItemModel(
                        year,
                        month,
                        day,
                        pref.getInt("tempStartHour",-1),
                        pref.getInt("tempMinutes",0),
                        pref.getInt("tempSteps",0));
            }else {
                stepsItemModel = new StepsItemModel(yearNow,monthNow,dayNow,-1,0,0);
                cleanTempSteps();
            }
            return stepsItemModel;
        }

        public static void cleanTempSteps(){
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("tempStepsData", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }


        public static void setGoal(int goal){
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("rewardData", MODE_PRIVATE).edit();
            editor.putInt("goal",goal);
            editor.commit();
        }
        public static int getGoal(){
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("rewardData", MODE_PRIVATE);
            int goal = pref.getInt("goal",10000);
            return goal;
        }
        public static int getRecorder(){
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("rewardData", MODE_PRIVATE);
            int recorder = pref.getInt("recorder",0);
            return recorder;
        }
        public static int[] getRecorderDate(){
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("rewardData", MODE_PRIVATE);
            int[] date = new int[3];
            date[0] = pref.getInt("year",0);
            date[1] = pref.getInt("month",0);
            date[2] = pref.getInt("day",0);
            return date;
        }

        public static void setRecorder(int recorder,int year,int month,int day){
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("rewardData", MODE_PRIVATE).edit();
            editor.putInt("recorder",recorder);
            editor.putInt("year",year);
            editor.putInt("month",month);
            editor.putInt("day",day);
            editor.commit();
        }

}
