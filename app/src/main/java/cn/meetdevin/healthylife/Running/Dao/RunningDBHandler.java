package cn.meetdevin.healthylife.Running.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Pedometer.Dao.MyStepsDatabaseHelper;
import cn.meetdevin.healthylife.Pedometer.Model.StepsItemModel;
import cn.meetdevin.healthylife.Running.Model.RunningItemModel;


/**
 * Created by XinZh on 2017/5/1.
 */
public class RunningDBHandler {
    private static final int ignore = -1;
    private MyRunningDatabaseHelper myRunningDatabaseHelper;
    private SQLiteDatabase database;

    public RunningDBHandler(){
        myRunningDatabaseHelper = new MyRunningDatabaseHelper(MyApplication.getContext(),"RunningDataB.db",null,1);
        database = myRunningDatabaseHelper.getWritableDatabase();
    }

    /**
     * 查询跑步数据
     */
    public List<RunningItemModel> queryRunningData(int y,int m,int d){
        Cursor cursor;
        if(y==ignore||m==ignore||d==ignore){
            cursor = database.query("RunningData",null,null,null,null,null,null);
        }else {
            String s = "select * from RunningData where year=? and month=? and day=?";
            String[] selectArgs = {String.valueOf(y),String.valueOf(m),String.valueOf(d)};
            cursor = database.rawQuery(s,selectArgs);
        }

        List<RunningItemModel> list = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                int year = cursor.getInt(cursor.getColumnIndex("year"));
                int month = cursor.getInt(cursor.getColumnIndex("month"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                int second = cursor.getInt(cursor.getColumnIndex("second"));
                int distance = cursor.getInt(cursor.getColumnIndex("distance"));
                int isComplete = cursor.getInt(cursor.getColumnIndex("iscomplete"));

                list.add(new RunningItemModel(year,month,day,second,distance,isComplete));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     *  插入跑步数据
     */
    public void insertRunningData(RunningItemModel runningItemModel){
        ContentValues values = new ContentValues();
        // 开始组装第一条数据
        values.put("year", runningItemModel.getYear());
        values.put("month", runningItemModel.getMonth());
        values.put("day", runningItemModel.getDay());
        values.put("second",runningItemModel.getSecond());
        values.put("distance", runningItemModel.getDistance());
        values.put("iscomplete",runningItemModel.getisComplete());

        database.insert("RunningData", null, values); // 插入数据
        values.clear();
    }

//    /**
//     * 查询今日计步数据
//     */
//    public TodayStepsModel getTodaySteps(){
//
//        TodayStepsModel todayStepsModel = new TodayStepsModel();
//
//        Cursor cursor = database.query("Today",null,null,null,null,null,null);
//        if (cursor.moveToFirst()) {
//            do {
//                // 遍历Cursor对象，取出数据
//                int steps = cursor.getInt(cursor.getColumnIndex("steps"));
//                //float distance = cursor.getFloat(cursor.getColumnIndex("distance"));
//                int startHour = cursor.getInt(cursor.getColumnIndex("startHour"));
//                int minutes = cursor.getInt(cursor.getColumnIndex("minutes"));
//                //初始化1
//                todayStepsModel.addStepsItem(new StepsItemModel(steps,startHour,minutes));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        //初始化2
//        Calendar calendar = Calendar.getInstance();
//        int[] myDate = new int[3];
//        myDate[0] = calendar.get(Calendar.YEAR);
//        myDate[1] = calendar.get(Calendar.MONTH);
//        myDate[2] = calendar.get(Calendar.DAY_OF_MONTH);
//
//        todayStepsModel.setMyDate(myDate);
//        todayStepsModel.countTodayTotalSteps();
//        todayStepsModel.countTodayTotalMinutes();
//        return todayStepsModel;
//    }


//    /**
//     *  把今天的步数数据插入往日表,并且置空今日表
//     *  每日24 ：00 调用
//     */
//    public void insertFormerTodays(){
//
//        TodayStepsModel todayStepsModel = getTodaySteps();
//        int[] myDate = todayStepsModel.getMyDate();
//        String date = myDate[0] + "-" + myDate[1] + "-" + myDate[2];
//
//        //将数据放入values
//        ContentValues values = new ContentValues();
//        values.put("date", date);
//        values.put("totalSteps", todayStepsModel.getTodayTotalSteps());
//        values.put("totalMinutes",todayStepsModel.getTodayTotalMinutes());
//        //values.put("totalDistance",totalDistance);
//
//        database.insert("FormerDays", null, values);
//        database.delete("Today",null,null);
//        values.clear();
//    }

//    /**
//     *  统计当日数据
//     */
//    private ContentValues getTodayValues(){
//        ContentValues values = new ContentValues();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        Date date = new Date(System.currentTimeMillis());
//        int totalSteps = 0;
//        int totalMinutes = 0;
//        //int totalDistance = 0;
//
//        Cursor cursor = database.query("Today",null,null,null,null,null,null);
//        if (cursor.moveToFirst()) {
//            do {
//                // 遍历Cursor对象，取出数据
//                int steps = cursor.getInt(cursor.
//                        getColumnIndex("steps"));
//                //float distance = cursor.getFloat(cursor.
//                        //getColumnIndex("distance"));
//                int minutes = cursor.getInt(cursor.getColumnIndex
//                        ("minutes"));
//                //统计数据
//                totalSteps += steps;
//                totalMinutes += minutes;
//                //totalDistance += distance;
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        //将数据放入values
//        values.put("date", String.valueOf(date));
//        values.put("totalSteps",totalSteps);
//        values.put("totalMinutes",totalMinutes);
//        //values.put("totalDistance",totalDistance);
//        return values;
//    }

}

