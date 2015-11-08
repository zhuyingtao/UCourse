package com.ustc.gcsj.course;

import com.ustc.gcsj.ucoursenew.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class CourseTime extends Activity {

    @SuppressWarnings("unused")
    private TextView tv;
    private TimePicker tp;
    private Button b1;
    private int course_time_hour;
    private int course_time_min;
    private Intent intent;
    private Bundle bundle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_time);
        intent = this.getIntent();
        bundle = intent.getExtras();
        course_time_hour = bundle.getInt("course_time_hour");
        course_time_min = bundle.getInt("course_time_min");
        TPInit();

    }

    private void TPInit() {
        // TODO Auto-generated method stub
        tv = (TextView) findViewById(R.id.tv);
        b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(b1Lis);

        tp = (TimePicker) findViewById(R.id.tp);
        tp.setIs24HourView(true);
        tp.setCurrentHour(course_time_hour);
        tp.setCurrentMinute(course_time_min);
        tp.setOnTimeChangedListener(tpLis);
    }

    private OnClickListener b1Lis = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // setTitle(tp.getCurrentHour()+":"+tp.getCurrentMinute());
            course_time_hour = tp.getCurrentHour();
            course_time_min = tp.getCurrentMinute();

            bundle.putInt("course_time_hour", course_time_hour);
            bundle.putInt("course_time_min", course_time_min);
            System.out.println("ffffffffffff"
                    + bundle.getInt("course_time_hour") + "  "
                    + bundle.getInt("course_time_min"));
            intent.putExtras(bundle);
            CourseTime.this.setResult(1, intent);
            CourseTime.this.finish();
        }
    };

    private OnTimeChangedListener tpLis = new OnTimeChangedListener() {
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            /*
             * tv.setText("[" + hourOfDay + ":" + minute + "]" + "[" +
			 * view.getCurrentHour() + ":" + view.getCurrentMinute() + "]");
			 */
        }
    };
}
