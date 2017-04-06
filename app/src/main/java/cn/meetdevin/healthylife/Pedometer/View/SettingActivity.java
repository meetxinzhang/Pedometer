package cn.meetdevin.healthylife.Pedometer.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.meetdevin.healthylife.Pedometer.Dao.StepsDataSP;
import cn.meetdevin.healthylife.Pedometer.Presenter.PedometerService;
import cn.meetdevin.healthylife.R;

/**
 * Created by XinZh on 2017/4/6.
 */

public class SettingActivity extends Activity implements View.OnClickListener{
    EditText setting_goal_text;
    Button setting_goal_butt;
    Button stop_pedometer_butt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pedometer);
        setting_goal_text = (EditText) findViewById(R.id.setting_goal_text);
        setting_goal_butt = (Button) findViewById(R.id.setting_goal_butt);
        stop_pedometer_butt = (Button) findViewById(R.id.stop_pedometer_butt);

        setting_goal_text.setText(String.valueOf(StepsDataSP.getGoal()));
        setting_goal_butt.setOnClickListener(this);
        stop_pedometer_butt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_goal_butt:
                StepsDataSP.setGoal(Integer.parseInt(setting_goal_text.getText().toString()));
                setting_goal_text.setText("");
                break;
            case R.id.stop_pedometer_butt:
                PedometerService.stopService();
                break;
        }
    }

    //此activity的启动方法
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
