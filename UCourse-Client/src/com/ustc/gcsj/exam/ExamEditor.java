package com.ustc.gcsj.exam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.gcsj.ucoursenew.R;

public class ExamEditor extends Activity {
	private NumberPicker mDateSpinner;
	private NumberPicker mHourSpinner;
	private NumberPicker mMinuteSpinner;
	private TextView txt, timeCount, showAlert;
	private Button btn_tiqian;
	private Calendar mDate;
	private int mYear, mMonth, mDay;
	private int mHour, mMinute;
	private String date;
	private Handler mHandler = new Handler();// 全局handler
	int time = 0;// 时间差
	private String[] mDateDisplayValues = new String[7];
	private String[] week = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private static String[] remind = new String[] { "准时提醒", "提前30分钟", "提前1个小时",
			"提前12个小时", "提前1天", "提前3天" };
	private RadioOnClick radioOnClick = new RadioOnClick(0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.exam_editor);
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String examName = intent.getExtras().getString("examName");
		String examPlace = intent.getExtras().getString("examPlace");
		String examTime = intent.getExtras().getString("examTime");
		String examAlert = intent.getExtras().getString("examAlert");
		mDate = Calendar.getInstance();
		mYear = mDate.get(Calendar.YEAR);
		mMonth = mDate.get(Calendar.MONTH);
		mDay = mDate.get(Calendar.DAY_OF_MONTH);
		mHour = mDate.get(Calendar.HOUR_OF_DAY);
		mMinute = mDate.get(Calendar.MINUTE);

		mDateSpinner = (NumberPicker) this.findViewById(R.id.np11);
		mDateSpinner.setMaxValue(6);
		mDateSpinner.setMinValue(0);
		updateDateControl();
		mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);

		mHourSpinner = (NumberPicker) this.findViewById(R.id.np22);
		mHourSpinner.setMaxValue(23);
		mHourSpinner.setMinValue(0);
		mHourSpinner.setValue(mHour);
		mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

		mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np33);
		mMinuteSpinner.setMaxValue(59);
		mMinuteSpinner.setMinValue(0);
		mMinuteSpinner.setValue(mMinute);
		mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);

		txt = (TextView) this.findViewById(R.id.exam_editor_showtime);
		showAlert = (TextView) this.findViewById(R.id.exam_editor_showalert);
		showAlert.setText(examAlert);// 初始化提醒
		EditText et = (EditText) findViewById(R.id.exam_editor_course);
		et.setText(examName);
		EditText et1 = (EditText) findViewById(R.id.exam_editor_place);
		et1.setText(examPlace);
		TextView tv = (TextView) findViewById(R.id.exam_editor_showtime);
		tv.setText(examTime);

		btn_tiqian = (Button) this.findViewById(R.id.exam_editor_alert);
		btn_tiqian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlarmDialog();
			}
		});

		updateDateTime();
		this.setButtonOK();
	}

	public void setButtonOK() {
		Button button = (Button) findViewById(R.id.exam_editor_ok);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText et1 = (EditText) findViewById(R.id.exam_editor_course);
				EditText et2 = (EditText) findViewById(R.id.exam_editor_place);

				String examName = et1.getText().toString();
				String examPlace = et2.getText().toString();
				if (examPlace == null || examPlace.equals(""))
					examPlace = "0";
				String examTime = date;
				String examAlert = showAlert.getText().toString();

				if (examName == null || examName.length() == 0) {
					Toast.makeText(ExamEditor.this, "请填写完整信息!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				DBHelper db = new DBHelper();
				db.insertData(examName, examPlace, examTime, examAlert);

				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private NumberPicker.OnValueChangeListener mOnDateChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
			updateDateControl();
			updateDateTime();
		}
	};

	private NumberPicker.OnValueChangeListener mOnHourChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mHour = mHourSpinner.getValue();
			updateDateTime();
		}
	};

	private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mMinute = mMinuteSpinner.getValue();
			updateDateTime();
		}
	};

	private String getWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(mYear, mMonth, mDay);
		int number = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return week[number];
	}

	private void updateDateControl() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(mDate.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
		mDateSpinner.setDisplayedValues(null);
		for (int i = 0; i < 7; ++i) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			mDateDisplayValues[i] = (String) DateFormat.format("MM.dd EEEE",
					cal);
		}
		mDateSpinner.setDisplayedValues(mDateDisplayValues);
		mDateSpinner.setValue(7 / 2);
		mDateSpinner.invalidate();
	}

	public void updateDateTime() {
		mYear = mDate.get(Calendar.YEAR);
		mMonth = mDate.get(Calendar.MONTH);
		mDay = mDate.get(Calendar.DAY_OF_MONTH);
		mHour = mHourSpinner.getValue();
		mMinute = mMinuteSpinner.getValue();

		date = mYear + "-" + (getDateFormat(mMonth + 1)) + "-"
				+ getDateFormat(mDay) + " " + getDateFormat(mHour) + ":"
				+ getDateFormat(mMinute) + ":00";

		txt.setText("您选择的是：" + date + " " + getWeek());
	}

	public String getDateFormat(int time) {
		String date = time + "";
		if (time < 10) {
			date = "0" + date;
		}
		return date;
	}

	private void showAlarmDialog() {
		AlertDialog ad = new AlertDialog.Builder(ExamEditor.this)
				.setTitle("选择提醒时间")
				.setSingleChoiceItems(remind, radioOnClick.getIndex(),
						radioOnClick).create();
		ad.getListView();
		ad.show();
	}

	/**
	 * 点击单选框事件
	 * 
	 * @author xmz
	 * 
	 */
	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			setIndex(which);
			showAlert.setText(remind[index]);
			dialog.dismiss();
		}
	}

	class TimeCount implements Runnable {
		@Override
		public void run() {
			while (time > 0)// 整个倒计时执行的循环
			{
				time--;
				mHandler.post(new Runnable() // 通过它在UI主线程中修改显示的剩余时间
				{
					public void run() {
						timeCount.setText(getInterval(time));// 显示剩余时间
					}
				});
				try {
					Thread.sleep(1000);// 线程休眠一秒钟 这个就是倒计时的间隔时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// 下面是倒计时结束逻辑
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					timeCount.setText("设定的时间到。");
				}
			});
		}
	}

	/**
	 * 获取两个日期的时间差
	 */
	public static int getTimeInterval(String data) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		int interval = 0;
		try {
			Date currentTime = new Date();// 获取现在的时间
			Date beginTime = dateFormat.parse(data);
			interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));// 时间差
																						// 单位秒
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return interval;
	}

	/**
	 * 获取提前提醒的秒数
	 */
	public int getAlarmTiqian(String tiqian) {
		int time = 0;
		if (tiqian.equals(remind[0])) {
			time = 0;// 准时提醒
		} else if (tiqian.equals(remind[1])) {
			time = 30 * 60;// 提前30分钟
		} else if (tiqian.equals(remind[2])) {
			time = 60 * 60;// 提前60分钟
		} else if (tiqian.equals(remind[3])) {
			time = 12 * 60 * 60;// 提前12小时
		} else if (tiqian.equals(remind[4])) {
			time = 24 * 60 * 60;// 提前1天
		} else if (tiqian.equals(remind[5])) {
			time = 3 * 24 * 60 * 60;// 提前3天
		} else {
			time = 24 * 60 * 60;// 提前一天
		}
		return time;
	}

	/**
	 * 设定显示文字
	 */
	public static String getInterval(int time) {
		String txt = null;
		if (time >= 0) {
			long day = time / (24 * 3600);// 天
			long hour = time % (24 * 3600) / 3600;// 小时
			long minute = time % 3600 / 60;// 分钟
			long second = time % 60;// 秒

			txt = " 距离现在还有：" + day + "天" + hour + "小时" + minute + "分" + second
					+ "秒";
		} else {
			txt = "已过期";
		}
		return txt;
	}
}
