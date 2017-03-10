package cn.meetdevin.healthylife.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by XinZh on 2017/3/8.
 */

public class MyStepsDatabaseHelper extends SQLiteOpenHelper{
    public static final String CREATE_TABLE_FORMERDAYS = "create table FormerDays ("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "totalSteps integer, "
            + "totalMinutes integer, "
            + "totalDistance real)";

    public static final String CREATE_TABLE_TODAY = "create table Today ("
            + "id integer primary key autoincrement, "
            + "steps integer, "
            + "distance real, "
            + "minutes integer)";


    public MyStepsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_TABLE_FORMERDAYS);
        db.execSQL(CREATE_TABLE_TODAY);
        Log.d("MyStepsDatabaseHelper", "onCreate: 数据库创建成功");
    }

    /**
     * 每日更新
     */
    public void upDateEveryDay(SQLiteDatabase db){
        db.execSQL("drop table if exists Today");
        db.execSQL(CREATE_TABLE_TODAY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists FormerDays");
        db.execSQL("drop table if exists today");
        onCreate(db);
    }
}
