package com.ustc.gcsj.course;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ustc.gcsj.ucoursenew.R;

@SuppressLint("ValidFragment")
public class Day extends Fragment {
    // 控件数据区
    private ListView lv;
    private View view;

    // 数据库数据区
    private CourseDB mCourseDB;
    private CourseInfoDB mCourseInfoDB;
    private Cursor mCoCursor;
    private Cursor mCiCursor;

    // 获取&显示的数据

    // 其它数据库
    private int daynumber;

    private String className;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_day_list, container, false);
        lv = (ListView) view.findViewById(R.id.course_list);
        mCourseDB = new CourseDB(getActivity());
        mCourseInfoDB = new CourseInfoDB(getActivity());
        mCiCursor = mCourseInfoDB.selectByDay(daynumber);
        initform();
        return view;
    }

    private void initform() {
        // TODO Auto-generated method stub
        final List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        this.initItems(items);

        String[] from = {"classNo", "className", "classRoom", "classTime"};
        int[] to = {R.id.classNo, R.id.className, R.id.classRoom,
                R.id.classTime};

        SimpleAdapter sa = new SimpleAdapter(view.getContext(), items,
                R.layout.course_item, from, to);
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new OnItemClickListener() {
            TextView oldtv = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String className = (String) items.get(position)
                        .get("className");
                // System.out.println("className = " + className);
                if (className == null) {
                    LinearLayout ll = (LinearLayout) view;
                    System.out.println("position:" + position);
                    LinearLayout lll = (LinearLayout) ll.getChildAt(1);
                    TextView tv = (TextView) lll.getChildAt(1);
                    tv.setTextColor(0xff669900);
                    tv.setTextSize(20);
                    if (tv.getText().toString().equals("")) {
                        String temp = "＋点击添加新课程";
                        tv.setText(temp);
                        if (oldtv != null
                                && oldtv.getText().toString().equals(temp))
                            oldtv.setText("");
                        oldtv = tv;
                    } else {
                        Intent intent = new Intent(getActivity(),
                                CourseAdd.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("show", className);
                    intent.setClass(getActivity(), CourseShow.class);
                    startActivity(intent);
                }
            }

        });
    }

    private void initItems(List<Map<String, Object>> items) {
        // TODO Auto-generated method stub
        int total = mCiCursor.getCount();
        if (total != 0) {

            for (mCiCursor.moveToFirst(); !mCiCursor.isAfterLast(); mCiCursor
                    .moveToNext()) {
                int startclass = mCiCursor.getInt(mCiCursor
                        .getColumnIndex("course_class_start"));
                int endclass = mCiCursor.getInt(mCiCursor
                        .getColumnIndex("course_class_end"));
                String classNo = new DecimalFormat("00").format(startclass + 1)
                        + " - " + new DecimalFormat("00").format(endclass + 1);
                className = mCiCursor.getString(mCiCursor
                        .getColumnIndex("course_name"));
                String classRoom = null;
                mCoCursor = mCourseDB.selectByName(className);
                if (mCoCursor.moveToFirst())
                    classRoom = mCoCursor.getString(mCoCursor
                            .getColumnIndex("course_room"));
                int classhour = mCiCursor.getInt(mCiCursor
                        .getColumnIndex("course_time_hour"));
                int classmin = mCiCursor.getInt(mCiCursor
                        .getColumnIndex("course_time_min"));
                String classTime = Integer.toString(classhour) + " : "
                        + new DecimalFormat("00").format(classmin);

                Map<String, Object> item = new HashMap<String, Object>();
                item.put("classNo", classNo);
                item.put("className", className);
                item.put("classRoom", "※ 地点 ：" + classRoom);
                item.put("classTime", "※ 时间 ：" + classTime);
                items.add(item);

            }
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("classNo", "");
            items.add(item);
        } else {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("classNo", "");
            items.add(item);
        }
    }

    public Day(int daynum) {
        this.daynumber = daynum - 1;
        System.out.println("*****" + daynumber);
    }
}
