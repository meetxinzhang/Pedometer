package cn.meetdevin.mbarchartopenlib;

/**
 * Created by XinZh on 2017/3/29.
 */

public class DataMod {
    private String year;
    private String month;
    private String day;
    private int startHour;
    private int mintues;
    private int val;

    public DataMod(int year,int month,int day,int startHour,int mintues,int val){
        this.year = String.valueOf(year);
        this.month = String.valueOf(month);
        this.day = String.valueOf(day);
        this.startHour = startHour;
        this.mintues = mintues;
        this.val = val;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMintues(int mintues) {
        this.mintues = mintues;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getMintues() {
        return mintues;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getVal() {
        return val;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}

