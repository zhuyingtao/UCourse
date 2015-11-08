package com.ustc.gcsj.settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ustc.gcsj.course.CourseDay;
import com.ustc.gcsj.function.Function;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.slidingmenu.SlideLeft;
import com.ustc.gcsj.ucoursenew.R;

public class Setting extends Fragment {

    private View view;
    private MainFrag myactivity = MainFrag.activity;
    private Function func = new Function();
    private EditText text_week_current;
    private Spinner spinner_week_total;
    private Spinner spinner_class_total;
    private Button button_exam_switch;
    private Button button_class_switch;
    private Button button_yes;
    private Button button_no;
    private String[] week_total_array = {"9周", "10周", "11周", "12周", "13周",
            "14周", "15周", "16周", "17周", "18周", "19周", "20周"};
    private String[] class_total_array = {"6节", "7节", "8节", "9节", "10节",
            "11节", "12节", "13节", "14节", "15节"};

    private int setting_current_week;
    private boolean setting_exam_switch;
    private boolean setting_course_switch;
    private String recordDate;
    private int recordtotalweek;
    private int recordtotalclass;

    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Date now = new Date();
    private Date start = null;
    private DateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat datef = DateFormat.getDateInstance();

    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting, container, false);
        sharedPreferences = getActivity().getSharedPreferences("setting",
                Context.MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();

        getData();
        viewInit();
        return view;
    }

    private void viewInit() {
        // TODO Auto-generated method stub
        // spinner 初始化
        spinner_week_total = (Spinner) view.findViewById(R.id.week_total);
        adapter1 = new ArrayAdapter<String>(getActivity(),
                R.layout.simple_spinner_item, week_total_array);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_week_total.setAdapter(adapter1);
        spinner_week_total
                .setOnItemSelectedListener(new SpinnerSelectedListener1());
        spinner_week_total.setVisibility(View.VISIBLE);
        spinner_week_total.setSelection(recordtotalweek);

        spinner_class_total = (Spinner) view.findViewById(R.id.class_total);
        adapter2 = new ArrayAdapter<String>(getActivity(),
                R.layout.simple_spinner_item, class_total_array);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class_total.setAdapter(adapter2);
        spinner_class_total
                .setOnItemSelectedListener(new SpinnerSelectedListener2());
        spinner_class_total.setVisibility(View.VISIBLE);
        spinner_class_total.setSelection(recordtotalclass);

        // edittext初始化
        text_week_current = (EditText) view.findViewById(R.id.week_current);
        try {
            start = fdate.parse(recordDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setting_current_week = func.computeWeeks(start, now)
                + setting_current_week;
        text_week_current.setText(Integer.toString(setting_current_week));

        // button初始化
        button_exam_switch = (Button) view.findViewById(R.id.exam_start);
        if (setting_exam_switch == false) {
            button_exam_switch.setText("关闭");
        } else {
            button_exam_switch.setText("启用");
        }
        button_exam_switch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (button_exam_switch.getText() == "启用") {
                    setting_exam_switch = false;
                    button_exam_switch.setText("关闭");

                } else {
                    setting_exam_switch = true;
                    button_exam_switch.setText("启用");
                }
            }
        });

        button_class_switch = (Button) view.findViewById(R.id.class_start);
        if (setting_course_switch == false) {
            button_class_switch.setText("关闭");
        } else {
            button_class_switch.setText("启用");
        }
        button_class_switch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (button_class_switch.getText() == "启用") {
                    button_class_switch.setText("关闭");
                    setting_course_switch = false;
                } else {
                    button_class_switch.setText("启用");
                    setting_course_switch = true;
                }
            }

        });

        button_yes = (Button) view.findViewById(R.id.setting_yes);
        button_no = (Button) view.findViewById(R.id.setting_no);
        button_yes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(text_week_current.getText().toString()) >= 1
                        && Integer.parseInt(text_week_current.getText()
                        .toString()) <= recordtotalweek + 9) {
                    setting_current_week = Integer.parseInt(text_week_current
                            .getText().toString());
                    recordDate = datef.format(now);
                    editor.putInt("setting_current_week", setting_current_week);
                    editor.putBoolean("setting_exam_switch",
                            setting_exam_switch);
                    editor.putBoolean("setting_course_switch",
                            setting_course_switch);
                    editor.putString("recordDate", recordDate);
                    editor.putInt("recordtotalweek", recordtotalweek);
                    editor.putInt("recordtotalclass", recordtotalclass);
                    editor.commit();
                    myactivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new CourseDay(-1))
                            .commit();
                    myactivity.getSlidingMenu().showContent();
                    SlideLeft.toggle = 0;
                } else {
                    Toast.makeText(getActivity(), "输入错误数据", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        button_no.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                myactivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag, new CourseDay(-1)).commit();
                myactivity.getSlidingMenu().showContent();
                SlideLeft.toggle = 0;
            }

        });

    }

    private void getData() {
        // TODO Auto-generated method stub

        setting_current_week = sharedPreferences.getInt("setting_current_week",
                0);
        setting_exam_switch = sharedPreferences.getBoolean(
                "setting_exam_switch", true);
        setting_course_switch = sharedPreferences.getBoolean(
                "setting_course_switch", true);
        recordDate = sharedPreferences.getString("recordDate",
                datef.format(now));
        recordtotalweek = sharedPreferences.getInt("recordtotalweek", 0);
        recordtotalclass = sharedPreferences.getInt("recordtotalclass", 0);
    }

    class SpinnerSelectedListener1 implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            // TODO Auto-generated method stub
            recordtotalweek = spinner_week_total.getSelectedItemPosition();
            // Toast.makeText(getActivity(), textView.getText(),
            // Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    class SpinnerSelectedListener2 implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            // TODO Auto-generated method stub
            recordtotalclass = spinner_class_total.getSelectedItemPosition();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

}
