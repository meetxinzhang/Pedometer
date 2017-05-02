package cn.meetdevin.healthylife.Running.Model;

/**
 * Created by XinZh on 2017/4/24.
 */

public class RunningItemModel {
    private int year;
    private int month;
    private int day;
    private int second;
    private int distance;
    private int isComplete = 0;// 0-未完成目标，1-完成目标

    public RunningItemModel(int year,int month,int day,int second,int distance,int isComplete){
        this.year = year;
        this.month = month;
        this.day = day;
        this.second = second;
        this.distance = distance;
        this.isComplete = isComplete;
    }
    public RunningItemModel(){}

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }


    public void setSecond(int second) {
        this.second = second;
    }


    public int getSecond() {
        return second;
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


    public int getDistance() {
        return distance;
    }


    public int getisComplete() {
        return isComplete;
    }

    public void setIsCompelet(int isCompelet) {
        this.isComplete = isCompelet;
    }

    public void clean(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.second = 0;
        this.distance = 0;
        this.isComplete = 0;
    }
}
