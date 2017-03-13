package cn.meetdevin.healthylife.Model;

import java.sql.Date;

/**
 * Created by XinZh on 2017/3/8.
 */

public class StepsItemModel {
    private int steps;
    private String startDate;
    private int minutes;
    private float distance;

    public StepsItemModel(int totalSteps,String startDate, int totalMinutes){
        this.startDate = startDate;
        this.steps = totalSteps;
        this.minutes = totalMinutes;
    }

    public StepsItemModel(int totalSteps, String startDate, int totalMinutes, int distance){
        this.startDate = startDate;
        this.steps = totalSteps;
        this.minutes = totalMinutes;
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSteps() {
        return steps;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void clean(){
        this.steps = 0;
        this.minutes = 0;
        this.startDate = "null";
    }
}
