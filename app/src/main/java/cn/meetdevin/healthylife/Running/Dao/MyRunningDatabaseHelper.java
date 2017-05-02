package cn.meetdevin.healthylife.Running.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by XinZh on 2017/5/1.
 */

public class MyRunningDatabaseHelper extends SQLiteOpenHelper{
    public static final String CREATE_TABLE_RUNNINGDATA= "create table RunningData ("
            + "id integer primary key autoincrement, "
            + "year integer, "
            + "month integer, "
            + "day integer, "
            + "second integer, "
            + "distance integer, "
            + "iscomplete integer)";


    public MyRunningDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_TABLE_RUNNINGDATA);
        Log.d("MyRunningDatabaseHelper", "onCreate: 数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists RunningData");
        onCreate(db);
    }
}
