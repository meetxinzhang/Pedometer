package cn.meetdevin.healthylife.Model;

/**
 * Created by XinZh on 2017/3/8.
 */

public class StepsItemModel {
    private int steps;
    private int minutes;
    private float distance;

    public StepsItemModel(int totalSteps, int totalMinutes){
        this.steps = totalSteps;
        this.minutes = totalMinutes;
    }

    public StepsItemModel(int totalSteps, int totalMinutes, int distance){
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
}
