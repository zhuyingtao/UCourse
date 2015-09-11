package com.ustc.gcsj.main;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.ustc.gcsj.course.CourseDay;
import com.ustc.gcsj.slidingmenu.SlideControl;
import com.ustc.gcsj.slidingmenu.SlideLeft;
import com.ustc.gcsj.ucoursenew.R;

public class MainFrag extends SlidingFragmentActivity {

	private SlideControl slidecontrol;
	private Boolean isExit = false;
	public static MainFrag activity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_frag);
		activity = this;
		slidecontrol = new SlideControl();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_frag, new CourseDay(-1)).commit();
	}

	public SlideControl getSlideControl() {
		return slidecontrol;

	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private void exitBy2Click() {
		Timer tExit = null;
		if (SlideLeft.toggle == 0) {
			if (isExit == false) {
				isExit = true; // 准备退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				tExit = new Timer();
				tExit.schedule(new TimerTask() {
					@Override
					public void run() {
						isExit = false; // 取消退出
					}
				}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

			} else {
				finish();
				System.exit(0);
			}
		} else {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_frag, new CourseDay(-1)).commit();
			getSlidingMenu().showContent();
			getSlideControl().setMode(false);
			SlideLeft.toggle = 0;
		}
	}

}
