package cn.meetdevin.healthylife.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.meetdevin.healthylife.Presenter.PedometerService;
import cn.meetdevin.healthylife.R;


public class MainActivity extends Activity implements View.OnClickListener{
    Button gotoSteps_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startStepsService();

        gotoSteps_b = (Button) findViewById(R.id.gotoSteps_b);
        gotoSteps_b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gotoSteps_b:
                StepsActivity.actionStart(this);break;
            default:
                break;
        }
    }

    private void startStepsService(){
        Intent intent = new Intent(this, PedometerService.class);
        startService(intent);
    }
}
