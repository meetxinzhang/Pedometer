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
    String date;

    public TodayStepsModel(String date, int todayTotalSteps,int todayTotalMinutes){
        list = new ArrayList<>();

        this.date = date;
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

    public int getTodayTotalSteps() {
        for (int i=0;i<list.size();i++){
            todayTotalSteps += list.get(i).getSteps();
        }
        return todayTotalSteps;
    }

    public int getTodayTotalMinutes() {
        for (int i=0;i<list.size();i++){
            todayTotalMinutes += list.get(i).getMinutes();
        }
        return todayTotalMinutes;
    }
}
