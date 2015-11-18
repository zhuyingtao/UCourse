package com.ustc.gcsj.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustc.gcsj.course.CourseDay;
import com.ustc.gcsj.doc.FileList;
import com.ustc.gcsj.doc.RightSlide;
import com.ustc.gcsj.exam.ExamList;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.note.NoteAll;
import com.ustc.gcsj.note.RightSlideNote;
import com.ustc.gcsj.settings.Setting;
import com.ustc.gcsj.ucoursenew.R;

public class SlideLeft extends Fragment {

    public static int toggle = -1;
    private ListView lv;
    private MainFrag myactivity = MainFrag.activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_slide, container, false);
        lv = (ListView) view.findViewById(R.id.slidelistView);

        // 所有的列表项
        final List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        this.initItems(items);

        String[] from = {"viewname"};
        int[] to = {R.id.viewname};
        SimpleAdapter sa = new SimpleAdapter(view.getContext(), items,
                R.layout.left_slide_item, from, to);
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        if (toggle == 0)
                            myactivity.getSlidingMenu().toggle();
                        else {
                            myactivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_frag, new CourseDay(-1))
                                    .commit();
                            myactivity.getSlidingMenu().showContent();
                            myactivity.getSlideControl().setMode(false);
                            toggle = 0;
                        }
                        break;
                    case 1:
                        if (toggle == 1) {
                            myactivity.getSlidingMenu().toggle();
                        } else {
                            myactivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_frag, new FileList())
                                    .commit();
                            myactivity.getSlidingMenu().showContent();
                            myactivity.getSlideControl().setMode(true);
                            myactivity.getSlideControl().initRightSlide(
                                    R.layout.slide_right, R.id.slide_right,
                                    new RightSlide());
                            toggle = 1;
                        }
                        break;
                    case 2:
                        if (toggle == 2) {
                            myactivity.getSlidingMenu().toggle();
                        } else {
                            myactivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_frag, new NoteAll())
                                    .commit();
                            myactivity.getSlidingMenu().showContent();
                            myactivity.getSlideControl().setMode(true);
                            myactivity.getSlideControl().initRightSlide(
                                    R.layout.main_slide2, R.id.main_slide2,
                                    new RightSlideNote());
                            toggle = 2;
                        }
                        break;
                    case 3:
                        if (toggle == 3) {
                            myactivity.getSlidingMenu().toggle();
                        } else {
                            myactivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_frag, new ExamList())
                                    .commit();
                            myactivity.getSlidingMenu().showContent();
                            myactivity.getSlideControl().setMode(true);
                            myactivity.getSlideControl().initRightSlide(
                                    R.layout.slide_right, R.id.slide_right,
                                    new RightSlide());
                            toggle = 3;
                        }
                        break;
                    case 4:
                        if (toggle == 4) {
                            myactivity.getSlidingMenu().toggle();
                        } else {
                            myactivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_frag, new Setting())
                                    .commit();
                            myactivity.getSlidingMenu().showContent();
                            myactivity.getSlideControl().setMode(false);
                            toggle = 4;
                        }
                }
            }
        });
        return view;
    }

    public void initItems(List<Map<String, Object>> items) {
        Map<String, Object> item = new HashMap<String, Object>();// 单个列表项组件
        item.put("viewname", "主页");
        items.add(item);
        item = new HashMap<String, Object>();
        item.put("viewname", "课件");
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("viewname", "笔记");
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("viewname", "考试");
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("viewname", "设置");
        items.add(item);
    }
}
