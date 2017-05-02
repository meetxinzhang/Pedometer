package cn.meetdevin.healthylife.Running.Dao;

import android.content.SharedPreferences;

import java.util.Calendar;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Pedometer.Model.StepsItemModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XinZh on 2017/3/31.
 */

public class RunningDataSP {

        public static void cleanRunningData(){
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("RunningData", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }


        public static void setRunningTimeGoal(int goal){
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("RunningData", MODE_PRIVATE).edit();
            editor.putInt("RunningTimeGoal",goal);
            editor.commit();
        }
        public static int getRunningTimeGoal(){
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("RunningData", MODE_PRIVATE);
            int goal = pref.getInt("RunningTimeGoal",30);//分钟
            return goal;
        }
        public static int[] getRunningTimeRecorder(){
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("RunningData", MODE_PRIVATE);
            int hour = pref.getInt("hour",0);
            int minute = pref.getInt("minute",0);
            int second = pref.getInt("second",0);
            return new int[]{hour,minute,second};
        }
        public static int[] getRunningTimeRecorderDate(){
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("RunningData", MODE_PRIVATE);
            int[] date = new int[3];
            date[0] = pref.getInt("year",0);
            date[1] = pref.getInt("month",0);
            date[2] = pref.getInt("day",0);
            return date;
        }

        public static void setRunningTimeRecorder(int hour,int minute,int second,int year,int month,int day){
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("RunningData", MODE_PRIVATE).edit();
            editor.putInt("year",year);
            editor.putInt("month",month);
            editor.putInt("day",day);
            editor.putInt("hour",hour);
            editor.putInt("minute",minute);
            editor.putInt("second",second);
            editor.commit();
        }



}
