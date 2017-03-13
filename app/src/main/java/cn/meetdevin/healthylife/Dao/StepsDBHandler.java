package cn.meetdevin.healthylife.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.meetdevin.healthylife.Model.StepsItemModel;
import cn.meetdevin.healthylife.Model.TodayStepsModel;
import cn.meetdevin.healthylife.config.MyApplication;


/**
 * Created by XinZh on 2017/3/8.
 */
public class StepsDBHandler {
    private MyStepsDatabaseHelper myStepsDatabaseHelper;
    private SQLiteDatabase database;

    public StepsDBHandler(){
        myStepsDatabaseHelper = new MyStepsDatabaseHelper(MyApplication.getContext(),"Steps.db",null,1);
        database = myStepsDatabaseHelper.getWritableDatabase();
    }

    /**
     * 查询往日计步数据
     */
    public List<TodayStepsModel> getFormerSteps(){
        List<TodayStepsModel> list = new ArrayList();
        Cursor cursor = database.query("FormerDays",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int totalSteps = cursor.getInt(cursor.getColumnIndex("totalSteps"));
                //float totalDistance = cursor.getFloat(cursor.getColumnIndex("totalDistance"));
                int totalMinutes = cursor.getInt(cursor.getColumnIndex("totalMinutes"));

                //list.add(new StepsItemModel(date,totalSteps,totalMinutes,totalDistance));
                int []myDate = new int[3];
                String []source = date.split("-",3);
                for (int i=0;i<source.length;i++){
                    myDate[i] = Integer.valueOf(source[i]);
                }
                list.add(new TodayStepsModel(myDate,totalSteps,totalMinutes));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 查询今日计步数据
     */
    public TodayStepsModel getTodaySteps(){

        TodayStepsModel todayStepsModel = new TodayStepsModel();

        Cursor cursor = database.query("Today",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                int steps = cursor.getInt(cursor.getColumnIndex("steps"));
                //float distance = cursor.getFloat(cursor.getColumnIndex("distance"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                int minutes = cursor.getInt(cursor.getColumnIndex("minutes"));
                //初始化1
                todayStepsModel.addStepsItem(new StepsItemModel(steps,startDate,minutes));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //初始化2
        Calendar calendar = Calendar.getInstance();
        int[] myDate = new int[3];
        myDate[0] = calendar.get(Calendar.YEAR);
        myDate[1] = calendar.get(Calendar.MONTH);
        myDate[2] = calendar.get(Calendar.DAY_OF_MONTH);

        todayStepsModel.setMyDate(myDate);
        todayStepsModel.countTodayTotalSteps();
        todayStepsModel.countTodayTotalMinutes();
        return todayStepsModel;
    }

    /**
     *  插入当日表
     */
    public void insertToday(StepsItemModel stepsItemModel){
        ContentValues values = new ContentValues();
        // 开始组装第一条数据
        values.put("steps", stepsItemModel.getSteps());
        //values.put("distance", distance);
        values.put("startDate",stepsItemModel.getStartDate());
        values.put("minutes", stepsItemModel.getMinutes());
        database.insert("Today", null, values); // 插入第一条数据
        values.clear();
    }

    /**
     *  把今天的步数数据插入往日表,并且置空今日表
     *  每日24 ：00 调用
     */
    public void insertFormerTodays(){

        TodayStepsModel todayStepsModel = getTodaySteps();
        int[] myDate = todayStepsModel.getMyDate();
        String date = myDate[0] + "-" + myDate[1] + "-" + myDate[2];

        //将数据放入values
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("totalSteps", todayStepsModel.getTodayTotalSteps());
        values.put("totalMinutes",todayStepsModel.getTodayTotalMinutes());
        //values.put("totalDistance",totalDistance);

        database.insert("FormerDays", null, values);
        database.delete("Today",null,null);
        values.clear();
    }

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

