package cn.meetdevin.healthylife.Pedometer.Dao;

import android.content.SharedPreferences;

import java.util.List;

import cn.meetdevin.healthylife.MyApplication;
import cn.meetdevin.healthylife.Pedometer.Model.StepsItemModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XinZh on 2017/3/31.
 */

public class StepsDataSP {

        public static void save(StepsItemModel stepsItemModel) {
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("tempStepsData", MODE_PRIVATE).edit();
            editor.putInt("tempYear", stepsItemModel.getYear());
            editor.putInt("tempMonth", stepsItemModel.getMonth());
            editor.putInt("tempDay", stepsItemModel.getDay());
            editor.putInt("tempStartHour", stepsItemModel.getStartHour());
            editor.putInt("tempMinutes", stepsItemModel.getMinutes());
            editor.putInt("tempSteps", stepsItemModel.getSteps());

            editor.commit();
        }

        public static StepsItemModel recovery() {
            SharedPreferences pref = MyApplication.getContext().getSharedPreferences("tempStepsData", MODE_PRIVATE);
            StepsItemModel stepsItemModel = new StepsItemModel(
                    pref.getInt("tempYear",0),
                    pref.getInt("tempMonth",0),
                    pref.getInt("tempDay",0),
                    pref.getInt("tempStartHour",0),
                    pref.getInt("tempMinutes",0),
                    pref.getInt("tempSteps",0));
            pref.edit().clear();//清除数据

            return stepsItemModel;
        }

}
