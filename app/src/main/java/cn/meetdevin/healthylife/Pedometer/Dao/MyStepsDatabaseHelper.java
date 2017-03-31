package cn.meetdevin.healthylife.Pedometer.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by XinZh on 2017/3/8.
 */

public class MyStepsDatabaseHelper extends SQLiteOpenHelper{
    public static final String CREATE_TABLE_STEPSDATA= "create table StepsData ("
            + "id integer primary key autoincrement, "
            + "year integer, "
            + "month integer, "
            + "day integer, "
            + "startHour integer, "
            + "minutes integer, "
            + "steps integer, "
            + "distance real)";


    public MyStepsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_TABLE_STEPSDATA);
        Log.d("MyStepsDatabaseHelper", "onCreate: 数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists StepsData");
        onCreate(db);
    }
}
