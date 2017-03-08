package cn.meetdevin.healthylife.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.meetdevin.healthylife.Model.MyStepsModel;


/**
 * Created by XinZh on 2017/3/8.
 */

public class StepsDBHandler {
    private Context context;
    private MyStepsDatabaseHelper myStepsDatabaseHelper;
    private SQLiteDatabase database;

    public StepsDBHandler(Context context){
        this.context = context;
        myStepsDatabaseHelper = new MyStepsDatabaseHelper(context,"Steps.db",null,1);
        database = myStepsDatabaseHelper.getReadableDatabase();
    }

    /**
     * 查询往日计步数据
     */
    public List<MyStepsModel> getFormerSteps(){
        List<MyStepsModel> list = new ArrayList();
        Cursor cursor = database.query("FormerDays",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                String date = cursor.getString(cursor.getColumnIndex("theDate"));
                int totalSteps = cursor.getInt(cursor.getColumnIndex("totalSteps"));
                float totalDistance = cursor.getFloat(cursor.getColumnIndex("totalDistance"));
                int totalMinutes = cursor.getInt(cursor.getColumnIndex("totalMinutes"));

                list.add(new MyStepsModel(date,totalSteps,totalMinutes,totalDistance));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 查询今日计步数据
     */
    public MyStepsModel getTodaySteps(){
        ContentValues values = getTodayValues();
        int totalSteps = (int) values.get("totalSteps");
        int totalMinutes = (int) values.get("totalMinutes");
        float totalDistance = (float) values.get("totalDistance");

        MyStepsModel myStepsModel = new MyStepsModel(totalSteps,totalMinutes,totalDistance);
        return myStepsModel;
    }

    /**
     *  插入当日表
     */
    public void insertToday(int steps, int minutes, float distance){
        ContentValues values = new ContentValues();
        // 开始组装第一条数据
        values.put("steps", steps);
        values.put("distance", distance);
        values.put("minutes", minutes);
        database.insert("Today", null, values); // 插入第一条数据
        values.clear();
    }

    /**
     *  把今天的步数数据插入往日表
     */
    public void insertFormerTodays(){
        ContentValues values = getTodayValues();
        database.insert("FormerDays", null, values);
        values.clear();
        myStepsDatabaseHelper.upDateEveryDay(database);
    }

    /**
     *  统计当日数据
     */
    private ContentValues getTodayValues(){
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date(System.currentTimeMillis());
        int totalSteps = 0;
        int totalMinutes = 0;
        int totalDistance = 0;

        Cursor cursor = database.query("Today",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                int steps = cursor.getInt(cursor.
                        getColumnIndex("steps"));
                float distance = cursor.getFloat(cursor.
                        getColumnIndex("distance"));
                int minutes = cursor.getInt(cursor.getColumnIndex
                        ("minutes"));
                //统计数据
                totalSteps += steps;
                totalMinutes += minutes;
                totalDistance += distance;
            } while (cursor.moveToNext());
        }
        cursor.close();
        //将数据放入values
        values.put("date", String.valueOf(date));
        values.put("totalSteps",totalSteps);
        values.put("totalMinutes",totalMinutes);
        values.put("totalDistance",totalDistance);
        return values;
    }

}
