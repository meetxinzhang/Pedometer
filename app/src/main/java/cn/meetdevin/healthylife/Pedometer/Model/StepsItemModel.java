package cn.meetdevin.healthylife.Pedometer.Model;

/**
 * Created by XinZh on 2017/3/8.
 */

public class StepsItemModel {
    private int steps;
    private int startHour;
    private int minutes;
    private float distance;

    public StepsItemModel(int totalSteps,int startHour, int totalMinutes){
        this.startHour = startHour;
        this.steps = totalSteps;
        this.minutes = totalMinutes;
    }

    public StepsItemModel(int totalSteps, int startHour, int totalMinutes, int distance){
        this.startHour = startHour;
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

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void clean(){
        this.steps = 0;
        this.minutes = 0;
        this.startHour = 0;
    }
}
