package com.ustc.gcsj.exam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ustc.gcsj.ucoursenew.R;

public class ExamList extends Fragment {

    private ListView lv;
    private View view;
    private DBHelper db;
    private List<Map<String, Object>> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.exam_list, container, false);
        lv = (ListView) view.findViewById(R.id.exam_list_listview);
        if (db == null)
            db = new DBHelper();
        this.showList();
        this.setExamAdder();
        this.setListItem();
        return view;
    }

    public void setExamAdder() {
        ImageButton ib = (ImageButton) view.findViewById(R.id.exam_list_adder);
        ib.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub\
                Intent intent = new Intent(getActivity(), ExamAdder.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "添加考试成功 ！", Toast.LENGTH_SHORT)
                    .show();
            this.showList();
        }
    }

    public void showList() {
        list = this.getList();
        String[] from = {"examName", "examPlace1", "examTime1", "examAlert",
                "examState"};
        int[] to = {R.id.exam_name, R.id.exam_place, R.id.exam_time,
                R.id.exam_alert, R.id.exam_state};
        SimpleAdapter sa = new SimpleAdapter(view.getContext(), list,
                R.layout.exam_item, from, to);
        lv.setAdapter(sa);
    }

    public void setListItem() {

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String examName = (String) list.get(position).get("examName");
                String examPlace = (String) list.get(position).get("examPlace");
                String examTime = (String) list.get(position).get("examTime");
                String examAlert = (String) list.get(position).get("examAlert");
                Intent intent = new Intent(getActivity(), ExamEditor.class);
                intent.putExtra("examName", examName);
                intent.putExtra("examPlace", examPlace);
                intent.putExtra("examTime", examTime);
                intent.putExtra("examAlert", examAlert);
                startActivityForResult(intent, 1);
            }
        });

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                final String examName = (String) list.get(position).get(
                        "examName");
                final String examTime = (String) list.get(position).get(
                        "examTime");
                // TODO Auto-generated method stub
                Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("删除提示框");
                alert.setMessage("确认删除该提醒？");
                alert.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                db.deleteData(examName, examTime);
                                Toast.makeText(view.getContext(), "删除考试成功 ！",
                                        Toast.LENGTH_SHORT).show();
                                showList();
                            }
                        });
                alert.setNegativeButton("取消", null);
                alert.show();
                return true;
            }
        });
    }

    public ArrayList<Map<String, Object>> getList() {

        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor = db.getData();
        if (cursor == null)
            return null;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String examName = cursor.getString(0);
            String examPlace = cursor.getString(1);
            if (examPlace.equals("0"))
                examPlace = "";
            String examTime = cursor.getString(2);
            String examAlert = cursor.getString(3);

            System.out.println("ExamList --> name : " + examName + " ; time : "
                    + examTime);
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("examName", examName);
            map.put("examPlace", examPlace);
            map.put("examTime", examTime);
            map.put("examAlert", examAlert);
            map.put("examState", "还剩" + getTimeInterval(examTime) + "天");
            map.put("examPlace1", "※地点 : " + examPlace);
            map.put("examTime1", "※时间 : " + examTime);
            list.add(map);
        }
        return list;
    }

    public static int getTimeInterval(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        int interval = 0;
        try {
            Date currentTime = new Date();// 获取现在的时间
            Date beginTime = dateFormat.parse(data);
            interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000) / 60 / 60 / 24);// 时间差
            // 单位秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return interval;
    }
}
