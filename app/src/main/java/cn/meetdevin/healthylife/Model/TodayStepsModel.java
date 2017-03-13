package cn.meetdevin.healthylife.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XinZh on 2017/3/10.
 */

public class TodayStepsModel {
    List<StepsItemModel> list;

    int todayTotalSteps;
    int todayTotalMinutes;
    //date
    int myDate[];

    public TodayStepsModel(int[] myDate ,int todayTotalSteps,int todayTotalMinutes){
        list = new ArrayList<>();

        this.myDate = myDate;
        this.todayTotalSteps = todayTotalSteps;
        this.todayTotalMinutes = todayTotalMinutes;
    }

    public TodayStepsModel(){
        list = new ArrayList<>();
    }

    public void addStepsItem(StepsItemModel StepsItemModel){
        list.add(StepsItemModel);
    }

    public List<StepsItemModel> getTodayStepsList(){
        return list;
    }

    public int countTodayTotalSteps() {
        todayTotalSteps = 0;
        for (int i=0;i<list.size();i++){
            todayTotalSteps += list.get(i).getSteps();
        }
        return todayTotalSteps;
    }

    public int countTodayTotalMinutes() {
        todayTotalMinutes = 0;
        for (int i=0;i<list.size();i++){
            todayTotalMinutes += list.get(i).getMinutes();
        }
        return todayTotalMinutes;
    }

    public int getTodayTotalMinutes() {
        return todayTotalMinutes;
    }

    public int getTodayTotalSteps() {
        return todayTotalSteps;
    }

    public void setMyDate(int[] myDate) {
        this.myDate = myDate;
    }

    public int[] getMyDate() {
        return myDate;
    }
}
