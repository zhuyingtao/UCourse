package com.ustc.gcsj.note;

import com.ustc.gcsj.course.CourseDB;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ShowNote extends Activity {
    private MainFrag myactivity = MainFrag.activity;
    private Spinner spinnerclassName;
    private ArrayAdapter<String> adapter;
    private EditText etnoteDetail; // 笔记详细信息
    private Button detBtn; // 删除笔记按钮
    private Button editBtn; // 编辑笔记按钮
    private String noteDetail;
    private String classNote;
    private NoteDB mNoteDB;
    private Cursor mCursor;
    private int noteID;
    private String noteIDD;
    private static String[] classes;
    private CourseDB mCourseDB;
    private Cursor mCoCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_show);

        mCourseDB = new CourseDB(this);
        mCoCursor = mCourseDB.select();
        classes = new String[mCoCursor.getCount()];
        getData();
        spinnerclassName = (Spinner) findViewById(R.id.spinnerclassName);
        // 将可选内容与ArrayAdapter连接
        adapter = new ArrayAdapter<String>(this,
                R.layout.course_classname_spinner_item, classes);
        // 设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter添加到spinner中
        spinnerclassName.setAdapter(adapter);

        etnoteDetail = (EditText) findViewById(R.id.etnote);
        mNoteDB = new NoteDB(this);
        mCursor = mNoteDB.select();

        Intent intent = getIntent();
        noteID = Integer.parseInt(intent.getStringExtra("noteID"));
        noteIDD = intent.getStringExtra("noteID");

        noteDetail = intent.getStringExtra("noteDetail");
        classNote = intent.getStringExtra("classNote");
        // System.out.println("kecehng"+classNote);

        for (int position = 0; position < spinnerclassName.getAdapter()
                .getCount(); position++) {
            if (classNote.endsWith((String) spinnerclassName.getAdapter()
                    .getItem(position)))
                spinnerclassName.setSelection(position);
        }
        spinnerclassName.setEnabled(false); // 使spinner处于不可编辑状态
        etnoteDetail.setText(noteDetail);

        detBtn = (Button) findViewById(R.id.deletebtn);
        editBtn = (Button) findViewById(R.id.editbtn);
        DeleteNote();
        EditNote();

    }

    void DeleteNote() {
        detBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ShowNote.this);
                builder.setTitle("    确定删除笔记？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @SuppressWarnings("deprecation")
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mNoteDB.delete(noteID);
                                mCursor.requery();
                                // BooksList.invalidateViews();
                                etnoteDetail.setText("");
                                Toast.makeText(ShowNote.this, "删除成功！",
                                        Toast.LENGTH_SHORT).show();
                                // 返回到所有笔记界面
                                myactivity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_frag, new NoteAll())
                                        .commitAllowingStateLoss();
                                finish();
                            }
                        });
                // 设置一个NegativeButton
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) // 点击取消时不做任何处理
                            {

                            }
                        });
                // 显示出该对话框
                builder.show();
            }
        });

    }

    void EditNote() { // 点击编辑笔记
        editBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ShowNote.this, EditNote.class);
                intent.putExtra("noteIDD", noteIDD);
                intent.putExtra("noteDetail", noteDetail);
                intent.putExtra("classNote", classNote); // 将当前页面的内容传到查看笔记页面中
                startActivity(intent);
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
}
