package cn.meetdevin.mbarchartopenlib;

/**
 * Created by XinZh on 2017/3/29.
 */

public class DataMod {
    private int info1;
    private int info2;
    private int info3;
    private int val;

    public DataMod(int info1,int info2,int info3,int val){
        this.info1 = info1;
        this.info2 = info2;
        this.info3 = info3;
        this.val = val;
    }


    public int getInfo1() {
        return info1;
    }

    public int getVal() {
        return val;
    }

    public int getInfo2() {
        return info2;
    }

    public int getInfo3() {
        return info3;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void setInfo1(int info1) {
        this.info1 = info1;
    }

    public void setInfo2(int info2) {
        this.info2 = info2;
    }

    public void setInfo3(int info3) {
        this.info3 = info3;
    }
}
