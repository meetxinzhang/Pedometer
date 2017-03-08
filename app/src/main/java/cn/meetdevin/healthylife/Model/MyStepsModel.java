package cn.meetdevin.healthylife.Model;

import java.sql.Date;

/**
 * Created by XinZh on 2017/3/8.
 */

public class MyStepsModel {
    private String date;
    private int steps;
    private int minutes;
    private float distance;

    public MyStepsModel(String date, int totalSteps, int totalMinutes, float totalDistance){
        this.date = date;
        this.steps = totalSteps;
        this.minutes = totalMinutes;
        this.distance = totalDistance;
    }

    public MyStepsModel(int steps, int minutes, float distance){
        this.steps = steps;
        this.distance = distance;
        this.minutes = minutes;
    }

    public String getTheDate() {
        return date;
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

    public void setDate(String date) {
        this.date = date;
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
}
