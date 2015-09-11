package com.ustc.gcsj.slidingmenu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.slidingmenu.lib.SlidingMenu;
import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

public class SlideControl {

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private MainFrag myactivity = MainFrag.activity;
	private SlidingMenu sm;

	public SlideControl() {
		// myactivity = ;
		fragmentManager = myactivity.getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		sm = myactivity.getSlidingMenu(); // 实例化滑动菜单对象
		initLeftSlide();
	}

	private void initLeftSlide() {
		// TODO Auto-generated method stub
		myactivity.setBehindContentView(R.layout.left_slide);
		fragmentTransaction.replace(R.id.left_slide, new SlideLeft()).commit();
		// 设置左边界面

		// 设置阴影
		sm.setShadowDrawable(R.drawable.shadow);
		// 设置阴影宽度
		sm.setShadowWidth(R.dimen.width);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.8f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

	}

	public void setMode(boolean mode) {
		if (mode == true) {
			sm.setMode(SlidingMenu.LEFT_RIGHT);
		} else {
			sm.setMode(SlidingMenu.LEFT);
		}
	}

	public void initRightSlide(int right_slide, int right_slide_id,
			Fragment SlideRight) {
		// 设置右边界面
		sm.setSecondaryMenu(right_slide);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		fragmentManager.beginTransaction().replace(right_slide_id, SlideRight)
				.commit();
	}
}
