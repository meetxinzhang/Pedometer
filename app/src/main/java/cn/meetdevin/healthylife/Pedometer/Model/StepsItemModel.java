package cn.meetdevin.healthylife.Pedometer.Model;

/**
 * Created by XinZh on 2017/3/8.
 */

public class StepsItemModel {
    private int year;
    private int month;
    private int day;
    private int startHour;
    private int minutes;
    private int steps;
    private float distance;

    public StepsItemModel(int year,int month,int day,int startHour, int minutes,int steps){
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        this.minutes = minutes;
        this.steps = steps;
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

    public void setYear(int year) {
        this.year = year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void clean(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.startHour = 0;
        this.minutes = 0;
        this.steps = 0;
        this.distance = 0;
    }
}
