package com.ustc.gcsj.note;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.ustc.gcsj.course.CourseDB;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

public class EditNote extends Activity {
	private MainFrag myactivity = MainFrag.activity;
	private Spinner spinnerclassName;
	private ArrayAdapter<String> adapter;
	private EditText etnoteDetail; // 笔记详细信息
	private Button resetBtn; // 取消编辑笔记按钮
	private Button submitBtn; // 确定按钮
	private String noteDetail;
	private String classNote;
	private NoteDB mNoteDB;
	private Cursor mCursor;
	private CourseDB mCourseDB;
	private Cursor mCoCursor;
	// private int noteID=0;
	private String noteIDD;
	private static String[] classes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_editor);
		mCourseDB = new CourseDB(this);
		mCoCursor = mCourseDB.select();
		classes = new String[mCoCursor.getCount()];
		getData();
		spinnerclassName = (Spinner) findViewById(R.id.spinnereditclassName);
		resetBtn = (Button) findViewById(R.id.resetbtn);
		submitBtn = (Button) findViewById(R.id.submitbtn);
		etnoteDetail = (EditText) findViewById(R.id.eteditnote);

		adapter = new ArrayAdapter<String>(this,
				R.layout.course_classname_spinner_item, classes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerclassName.setAdapter(adapter);

		mNoteDB = new NoteDB(this);
		mCursor = mNoteDB.select();
		mCursor.close();
		mNoteDB.close();

		Intent intent = getIntent();
		noteIDD = intent.getStringExtra("noteIDD");
		// System.out.println("这又是怎么了"+noteIDD);
		noteDetail = intent.getStringExtra("noteDetail");
		classNote = intent.getStringExtra("classNote");
		for (int position = 0; position < spinnerclassName.getAdapter()
				.getCount() - 1; position++) {
			if (classNote.endsWith((String) spinnerclassName.getAdapter()
					.getItem(position)))
				spinnerclassName.setSelection(position);
		}
		etnoteDetail.setText(noteDetail);

		ResetBtn();
		SubmitBtn();
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

	void ResetBtn() {
		resetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish(); // 回到笔记列表界面还是回到上一页面？
			}

		});

	}

	void SubmitBtn() {
		submitBtn.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				String nclassname = spinnerclassName.getSelectedItem()
						.toString();
				String notedetail = etnoteDetail.getText().toString();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日   HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String notedate = formatter.format(curDate);
				int noteID = Integer.parseInt(noteIDD);

				if (nclassname.equals("") || notedetail.equals("")) {
					return;
				}
				mNoteDB.update(noteID, nclassname, notedetail, notedate);
				mCursor.requery();
				Toast.makeText(getApplicationContext(), "修改成功",
						Toast.LENGTH_SHORT).show();
				// 返回到所有笔记界面
				myactivity.getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_frag, new NoteAll())
						.commitAllowingStateLoss();
				finish();

			}

		});

	}

}
