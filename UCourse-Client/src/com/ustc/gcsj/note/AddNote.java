package com.ustc.gcsj.note;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ustc.gcsj.course.CourseDB;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

public class AddNote extends Activity {
    private MainFrag myactivity = MainFrag.activity;
    private NoteDB mNoteDB;
    private Cursor mCursor;
    private Spinner className;
    private ArrayAdapter<String> adapter;
    private Button submitButton; // 添加笔记保存按钮
    private Button resetButton;
    private EditText addText;
    private String nclass;
    private static String[] classes;

    private CourseDB mCourseDB;
    private Cursor mCoCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_adder);
        className = (Spinner) findViewById(R.id.classNote1);
        addText = (EditText) findViewById(R.id.note);
        mCourseDB = new CourseDB(this);
        mCoCursor = mCourseDB.select();

        System.out.println("*********" + mCoCursor.getCount());

        classes = new String[mCoCursor.getCount()];
        getData();
        // 将可选内容与ArrayAdapter连接
        adapter = new ArrayAdapter<String>(this,
                R.layout.course_classname_spinner_item, classes);
        // 设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter添加到spinner中
        className.setAdapter(adapter);
        submitButton = (Button) findViewById(R.id.submitbtn);
        resetButton = (Button) findViewById(R.id.resetbtn);

        className.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                nclass = arg0.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                addNote();
                // finish();
                myactivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag, new NoteAll())
                        .commitAllowingStateLoss();
                finish();
            }

        });
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                myactivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag, new NoteAll())
                        .commitAllowingStateLoss();
                finish();

            }

        });

    }

    private void getData() {
        // TODO Auto-generated method stub
        mCoCursor.moveToFirst();
        for (int i = 0; i < mCoCursor.getCount(); i++) {
            classes[i] = mCoCursor.getString(1);
            mCoCursor.moveToNext();
            System.out.println("*********" + i);
        }

    }

    @SuppressWarnings("deprecation")
    public void addNote() {

        String nclassname = nclass;
        String notedetail = addText.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String notedate = formatter.format(curDate);
        if (notedetail.equals("") || nclassname.equals("")) {
            return;
        }
        mNoteDB = new NoteDB(this);
        mCursor = mNoteDB.select();
        mNoteDB.insert(nclassname, notedetail, notedate);
        mCursor.requery();
        addText.setText("");
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        mCursor.close();
        mNoteDB.close();

    }

}
