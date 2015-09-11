package com.ustc.gcsj.course;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.ustc.gcsj.function.Function;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;
//Intent intent = getIntent();
//className = intent.getStringExtra("show");

@SuppressLint("WorldReadableFiles")
public class CourseShow extends Activity {
	// 控件区
	private EditText text_course_name;
	private EditText text_teacher_name;
	private EditText text_course_room;
	private Spinner spinner_week_start, spinner_week_end;
	private Spinner spinner_day, spinner_class_start, spinner_class_end;
	private Button buttonyes, buttonno, buttonset;

	// preference数据接受传递区
	private int week_total_num;
	private int class_total_num;
	private String[] week;
	private String[] classnum;
	private String[] day = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };

	// 控件数据得到区
	private String course_name;
	private String course_room;
	private String course_teacher;
	private int course_week_start;
	private int course_week_end;

	private int course_time_hour;
	private int course_time_min;
	private int course_day;
	private int course_class_start;
	private int course_class_end;

	// 数据库数据区
	private CourseDB mCourseDB;
	private CourseInfoDB mCourseInfoDB;
	private Cursor mCoCursor;
	private Cursor mCiCursor;

	// 其它数据区
	private ArrayAdapter<String> adapter;
	private MainFrag myactivity = MainFrag.activity;
	private SharedPreferences sharedPreferences;
	@SuppressWarnings("unused")
	private SharedPreferences.Editor editor;
	private Function func = new Function();

	// 改变添加区
	private int course_id;
	private int courseinfo_id;
	private Button buttondelete;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_show);
		Intent intent = getIntent();
		course_name = intent.getStringExtra("show");
		sharedPreferences = getSharedPreferences("setting",
				Context.MODE_WORLD_READABLE);
		editor = sharedPreferences.edit();

		mCourseDB = new CourseDB(this);
		mCoCursor = mCourseDB.selectByName(course_name);

		mCourseInfoDB = new CourseInfoDB(this);
		mCiCursor = mCourseInfoDB.selectByName(course_name);

		getData();
		viewInit();
	}

	private void viewInit() {
		// TODO Auto-generated method stub
		// 初始化spinner
		Stringinitialize();
		spinner_week_start = (Spinner) findViewById(R.id.spinnerweekstart);
		adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
				week);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_week_start.setAdapter(adapter);
		spinner_week_start
				.setOnItemSelectedListener(new SpinnerSelectedListener(0));
		spinner_week_start.setVisibility(View.VISIBLE);
		spinner_week_start.setSelection(course_week_start);
		System.out.println("course_week_start" + course_week_start);

		spinner_week_end = (Spinner) findViewById(R.id.spinnerweekend);
		adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
				week);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_week_end.setAdapter(adapter);
		spinner_week_end.setOnItemSelectedListener(new SpinnerSelectedListener(
				1));
		spinner_week_end.setVisibility(View.VISIBLE);
		spinner_week_end.setSelection(course_week_end);

		spinner_day = (Spinner) findViewById(R.id.spinnerday);
		adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
				day);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_day.setAdapter(adapter);
		spinner_day.setOnItemSelectedListener(new SpinnerSelectedListener(2));
		spinner_day.setVisibility(View.VISIBLE);
		spinner_day.setSelection(course_day);
		System.out.println("course_day" + course_day);

		spinner_class_start = (Spinner) findViewById(R.id.spinnerclassstart);
		adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
				classnum);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_class_start.setAdapter(adapter);
		spinner_class_start
				.setOnItemSelectedListener(new SpinnerSelectedListener(3));
		spinner_class_start.setVisibility(View.VISIBLE);
		spinner_class_start.setSelection(course_class_start);
		System.out.println("course_class_start" + course_class_start);

		spinner_class_end = (Spinner) findViewById(R.id.spinnerclassend);
		adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
				classnum);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_class_end.setAdapter(adapter);
		spinner_class_end
				.setOnItemSelectedListener(new SpinnerSelectedListener(4));
		spinner_class_end.setVisibility(View.VISIBLE);
		spinner_class_end.setSelection(course_class_end);

		// 初始化text
		text_course_name = (EditText) findViewById(R.id.coursename);
		text_course_name.setText(course_name);
		text_teacher_name = (EditText) findViewById(R.id.teachername);
		text_teacher_name.setText(course_teacher);
		text_course_room = (EditText) findViewById(R.id.courseroom);
		text_course_room.setText(course_room);

		// 初始化button
		buttonset = (Button) findViewById(R.id.course_time_set);
		buttonset.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("course_time_hour", course_time_hour);
				bundle.putInt("course_time_min", course_time_min);
				Intent intent = new Intent();
				intent.setClass(CourseShow.this, CourseTime.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);
			}
		});
		buttonyes = (Button) findViewById(R.id.addcourseyes);
		buttonyes.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				course_name = text_course_name.getText().toString();
				course_teacher = text_teacher_name.getText().toString();
				course_room = text_course_room.getText().toString();
				// System.out.println(course_name==null);
				if (func.isEmpty(course_name) || func.isEmpty(course_teacher)
						|| func.isEmpty(course_room)) {

					Toast.makeText(getApplicationContext(), "输入错误数据",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(
							getApplicationContext(),
							course_name + " " + course_teacher + "  "
									+ course_room + "   " + course_class_start
									+ "   " + course_class_end,
							Toast.LENGTH_LONG).show();
					// Log.v("11111", course_name);
					mCourseDB.update(course_id, course_name, course_room,
							course_teacher, course_week_start, course_week_end);
					mCoCursor.requery();
					mCourseInfoDB.update(courseinfo_id, course_name,
							course_time_hour, course_time_min, course_day,
							course_class_start, course_class_end);
					mCiCursor.requery();
					myactivity
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.main_frag,
									new CourseDay(CourseDay.currIndex))
							.commitAllowingStateLoss();
					finish();
				}

			}
		});

		buttonno = (Button) findViewById(R.id.addcourseno);
		buttonno.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		buttondelete = (Button) findViewById(R.id.deletecourse);
		buttondelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mCourseDB.delete(course_id);
				mCourseInfoDB.delete(courseinfo_id);
				myactivity
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.main_frag,
								new CourseDay(CourseDay.currIndex))
						.commitAllowingStateLoss();
				finish();
			}
		});
	}

	private void getData() {
		// TODO Auto-generated method stub
		mCoCursor.moveToFirst();
		// while(mCoCursor.moveToNext())
		{
			course_id = mCoCursor.getInt(0);
			course_room = mCoCursor.getString(mCoCursor
					.getColumnIndex("course_room"));
			course_teacher = mCoCursor.getString(mCoCursor
					.getColumnIndex("course_teacher"));
			course_week_start = mCoCursor.getInt(mCoCursor
					.getColumnIndex("course_week_start"));
			course_week_end = mCoCursor.getInt(mCoCursor
					.getColumnIndex("course_week_end"));
		}
		mCiCursor.moveToFirst();
		// while(mCiCursor.moveToNext())
		{
			courseinfo_id = mCiCursor.getInt(0);
			course_time_hour = mCiCursor.getInt(mCiCursor
					.getColumnIndex("course_time_hour"));
			course_time_min = mCiCursor.getInt(mCiCursor
					.getColumnIndex("course_time_min"));
			course_day = mCiCursor.getInt(mCiCursor
					.getColumnIndex("course_day"));
			course_class_start = mCiCursor.getInt(mCiCursor
					.getColumnIndex("course_class_start"));
			course_class_end = mCiCursor.getInt(mCiCursor
					.getColumnIndex("course_class_end"));
			System.out.println("sdsasdasd" + course_day);
		}
		week_total_num = sharedPreferences.getInt("recordtotalweek", 0);
		class_total_num = sharedPreferences.getInt("recordtotalclass", 0);
	}

	private void Stringinitialize() {
		// TODO Auto-generated method stub
		week = new String[week_total_num + 9];
		System.out.println(week_total_num);
		for (int i = 0; i < week_total_num + 9; i++) {
			week[i] = "第" + (i + 1) + "周";
		}

		classnum = new String[class_total_num + 6];
		System.out.println(class_total_num);
		for (int i = 0; i < class_total_num + 6; i++) {
			classnum[i] = "第" + (i + 1) + "节";
		}
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		private int choice;

		SpinnerSelectedListener(int id) {
			choice = id;
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch (choice) {
			case 0:
				course_week_start = spinner_week_start
						.getSelectedItemPosition();
				break;
			case 1:
				course_week_end = spinner_week_end.getSelectedItemPosition();
				break;
			case 2:
				course_day = spinner_day.getSelectedItemPosition();
				break;
			case 3:
				course_class_start = spinner_class_start
						.getSelectedItemPosition();
				break;
			case 4:
				course_class_end = spinner_class_end.getSelectedItemPosition();
				break;

			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println("kkkkkkkkkkkkkkkk" + requestCode + "  ");
		switch (requestCode) {
		case 1:
			Bundle bundle = data.getExtras();
			course_time_hour = bundle.getInt("course_time_hour");
			course_time_min = bundle.getInt("course_time_min");
			System.out.println("kkkkkkkkkkkkkkkk"
					+ bundle.getInt("course_time_hour") + "  "
					+ bundle.getInt("course_time_min"));
		}

	}

}
