package cn.meetdevin.healthylife.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by XinZh on 2017/3/8.
 */

public class MyStepsDatabaseHelper extends SQLiteOpenHelper{
    public static final String CREATE_TABLE_FORMERDAYS = "create table FormerDays ("
            + "id date primary key autoincrement, "
            + "date text, "          //日期
            + "totalSteps integer, "    //一天的总步数
            + "totalMinutes integer, "  //总持续时间
            + "totalDistance real)";    //总距离

    public static final String CREATE_TABLE_TODAY = "create table Today ("
            + "id integer primary key autoincrement, "
            + "steps integer, "     //该时间段内步数
            + "distance real, "    //该时间段内距离
            + "minutes integer)";  //该次持续时间

    private Context mContext;

    public MyStepsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建往日数据表
        db.execSQL(CREATE_TABLE_FORMERDAYS);
        Toast.makeText(mContext, "往日数据库创建成功", Toast.LENGTH_SHORT).show();
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
        db.execSQL(CREATE_TABLE_FORMERDAYS);
        db.execSQL("drop table if exists Today");
        db.execSQL(CREATE_TABLE_TODAY);
    }
}
