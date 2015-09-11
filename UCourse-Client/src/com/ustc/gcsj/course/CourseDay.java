package com.ustc.gcsj.course;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

@SuppressLint("ValidFragment")
public class CourseDay extends Fragment {

	private ViewPager viewPager;// 页卡
	private ImageView imageView;// 动画图片
	private TextView textView1, textview2, textview3, textview4, textview5,
			textview6, textview7;// 周1-7文字
	private int offset = 0;// 动画图片偏移量
	public static int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private List<Fragment> frags;// Tab页面列表
	private View view;
	private Calendar c = Calendar.getInstance();
	private int currday = (Integer.parseInt(String.valueOf(c
			.get(Calendar.DAY_OF_WEEK))) + 6) % 7;;
	// private int currday =
	// (Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))+6)%7;
	private MainFrag myactivity = MainFrag.activity;
	private TextView titlecourse;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.course_day, container, false);
		// 初始化各个View
		currIndex = currday - 1;
		InitImageView();
		InitTextView();
		InitViewPager();
		return view;
	}

	public CourseDay(int i) {
		if (i == -1) {
			currday = (Integer.parseInt(String.valueOf(c
					.get(Calendar.DAY_OF_WEEK))) + 6) % 7;
		} else {
			currday = i + 1;
		}
	}

	public void onResume() {
		super.onResume();
	}

	private void InitTextView() {
		// TODO Auto-generated method stub
		textView1 = (TextView) view.findViewById(R.id.text1);
		textview2 = (TextView) view.findViewById(R.id.text2);
		textview3 = (TextView) view.findViewById(R.id.text3);
		textview4 = (TextView) view.findViewById(R.id.text4);
		textview5 = (TextView) view.findViewById(R.id.text5);
		textview6 = (TextView) view.findViewById(R.id.text6);
		textview7 = (TextView) view.findViewById(R.id.text7);
		titlecourse = (TextView) view.findViewById(R.id.titlecourse);
		// 自定义监听类：头标监听的构造函数
		textView1.setOnClickListener(new MyOnClickListener(0));
		textview2.setOnClickListener(new MyOnClickListener(1));
		textview3.setOnClickListener(new MyOnClickListener(2));
		textview4.setOnClickListener(new MyOnClickListener(3));
		textview5.setOnClickListener(new MyOnClickListener(4));
		textview6.setOnClickListener(new MyOnClickListener(5));
		textview7.setOnClickListener(new MyOnClickListener(6));
		titlecourse.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myactivity.getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_frag, new CourseDay(-1)).commit();
				myactivity.getSlidingMenu().showContent();
				myactivity.getSlideControl().setMode(false);
				// itemSelect =1;
			}
		});
	}

	// 初始化页卡
	private void InitViewPager() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) view.findViewById(R.id.mPager);
		frags = new ArrayList<Fragment>();
		// LayoutInflater inflater = getActivity().getLayoutInflater();
		frags.add(new Day(1));
		frags.add(new Day(2));
		frags.add(new Day(3));
		frags.add(new Day(4));
		frags.add(new Day(5));
		frags.add(new Day(6));
		frags.add(new Day(7));
		// 自定义pageadapter
		viewPager.setAdapter(new MyViewPagerAdapter(myactivity
				.getSupportFragmentManager()));
		viewPager.setCurrentItem(currIndex);
		// 自定义页面切换监听函数
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	// 初始化动画
	private void InitImageView() {
		// TODO Auto-generated method stub
		imageView = (ImageView) view.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.sign)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 7 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置

		/* 修改部分 */
		int offset_slide = offset * 2 + bmpW;
		Animation animation = new TranslateAnimation(offset_slide * currIndex,
				offset_slide * currIndex, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(300);
		imageView.startAnimation(animation);
	}

	// 头标监听
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewPager.setCurrentItem(index);// 跳转到该页面
		}
	}

	// 重写PagerAdapter
	private class MyViewPagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> views;

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			views = frags;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return views.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		int offset_slide = offset * 2 + bmpW;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			// dayText = (TextView) findViewById(R.id.daytext);
			// dayText.setText("今天是第"+(currIndex+1)+"天");
			// dayText.setText("今天是第"+((viewPager.getCurrentItem())+1)+"天");
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = new TranslateAnimation(offset_slide
					* currIndex, offset_slide * arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			// Toast.makeText(MainActivity.this, "您选择了"+
			// viewPager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();
		}

	}

}
