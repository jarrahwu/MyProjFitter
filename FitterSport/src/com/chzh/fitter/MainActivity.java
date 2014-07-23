package com.chzh.fitter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.chzh.fitter.fragment.FriendsFragment;
import com.chzh.fitter.fragment.HomeFragment;
import com.chzh.fitter.fragment.MeFragment;
import com.chzh.fitter.fragment.ScheduleFragment;
import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.framework.BasePopupWindow;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.util.DensityUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.MainTitleBar;
import com.chzh.fitter.view.SettingView;
import com.chzh.fitter.view.SwitchBarItemView;
import com.chzh.fitter.view.SwitchTabBar;
import com.chzh.fitter.view.SwitchTabBar.SwitchBarItemClickListener;

public class MainActivity extends BaseActivity implements
		SwitchBarItemClickListener, GlobalConstant {

	private static final int HOME = 0; // 首页
	private static final int SCHEDULE = 1; // 计划表
	private static final int FRIEND = 2; // 朋友
	private static final int ME = 3; // 我

	public static final int LOGOUT = 0xa1;// 用户注销

	/**
	 * 正在显示的fragment
	 */
	private Fragment mShowingFragment = null;

	// four sections
	private HomeFragment mHomeFragment;
	private ScheduleFragment mScheduleFragment;
	private FriendsFragment mFriendsFragment;
	private MeFragment mMeFragment;

	private SwitchTabBar mBottomBar;

	private MainTitleBar mTitleBar;

	private BasePopupWindow mSettingPopupWindow;

	private static boolean mIsResotreFragment;

	@Override
	protected void setupViews() {
		L.red(MainActivity.class, "setupViews");
		// 标题设置
		mTitleBar = new MainTitleBar(this, "首页");

		initInflater();
		setContentView(mTitleBar, R.layout.activity_main);
		// 设置默认背景
		setDefaultAppBackground();
		// 载入其他3个tab的 content
		initFragments();

		if (!mIsResotreFragment) {
			L.red(MainActivity.class, "handle fragments");
			addFragments();
			appear(mHomeFragment);
		}

		// tab bar
		mBottomBar = (SwitchTabBar) findViewById(R.id.bottomBar);
		mBottomBar.setSwitchBarItemClickListener(this);
		mBottomBar.highligtItemAt(HOME);

		// popup
		View parent = getWindow().getDecorView();
		SettingView settingView = new SettingView(this);
		mSettingPopupWindow = new BasePopupWindow(parent, settingView, true);
		settingView.setTag(mSettingPopupWindow);// setting 可以关闭popupwindow
		bindClickEvent(mTitleBar.getBackView(), "onSettingClicked");

		// baidu 统计code
		setupBaidu();
	}

	private void setupBaidu() {
		StatService.setAppKey(GlobalConstant.BAIDU_KEY);
		StatService.setAppChannel(this, "", false);
		StatService.setSessionTimeOut(30);
		StatService.setOn(this, StatService.EXCEPTION_LOG);
		StatService.setDebugOn(true);
	}

	@Override
	protected void restoreViews(Bundle savedInstanceState) {
		initInflater();
		// mHomeFragment = (HomeFragment)
		// getFragmentManager().getFragment(savedInstanceState,
		// HomeFragment.class.getName());
		// mScheduleFragment = (ScheduleFragment)
		// getFragmentManager().getFragment(savedInstanceState,
		// ScheduleFragment.class.getName());
		// mFriendsFragment = (FriendsFragment)
		// getFragmentManager().getFragment(savedInstanceState,
		// FriendsFragment.class.getName());
		// mMeFragment = (MeFragment)
		// getFragmentManager().getFragment(savedInstanceState,
		// MeFragment.class.getName());
		// mIsResotreFragment = true;
		L.tagNull(MainActivity.class, mHomeFragment, mScheduleFragment,
				mFriendsFragment, mMeFragment);
		setupViews();
		// setContentView(R.layout.activity_main);
		// removeOther3();
	}

//	private void removeOther3() {
//		findView(R.id.main_fragment_containter, ViewGroup.class)
//				.removeAllViews();
//		FragmentTransaction trans = getFragmentManager().beginTransaction();
//		trans.remove(mScheduleFragment);
//		trans.remove(mFriendsFragment);
//		trans.remove(mMeFragment);
//		trans.commit();
//	}

	private void addFragments() {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.add(R.id.main_fragment_containter, mHomeFragment,
				HomeFragment.class.getName());
		transaction.add(R.id.main_fragment_containter, mScheduleFragment,
				ScheduleFragment.class.getClass().getName());
		transaction.add(R.id.main_fragment_containter, mFriendsFragment,
				FriendsFragment.class.getName());
		transaction.add(R.id.main_fragment_containter, mMeFragment,
				MeFragment.class.getName());

		transaction.hide(mScheduleFragment);
		transaction.hide(mFriendsFragment);
		transaction.hide(mMeFragment);

		transaction.commit();
	}

	private void initFragments() {
		if (mHomeFragment == null)
			mHomeFragment = new HomeFragment();

		if (mScheduleFragment == null)
			mScheduleFragment = new ScheduleFragment();

		if (mFriendsFragment == null)
			mFriendsFragment = new FriendsFragment();

		if (mMeFragment == null)
			mMeFragment = new MeFragment();

		mShowingFragment = mHomeFragment;
	}

	public void onSettingClicked(View v) {
		if (mSettingPopupWindow.isShowing()) {
			mSettingPopupWindow.dismiss();
		} else
			mSettingPopupWindow.showAsDropDown(mTitleBar.getBackView(), 0,
					(int) DensityUtil.convertDpToPixel(15));

	}

	private void switchFragment(int key) {
		switch (key) {

		case HOME:
			mTitleBar.setTittle("首页");
			appear(mHomeFragment);
			break;

		case SCHEDULE:
			mTitleBar.setTittle("计划表");
			appear(mScheduleFragment);
			break;

		case FRIEND:
			mTitleBar.setTittle("朋友");
			appear(mFriendsFragment);
			break;

		case ME:
			mTitleBar.setTittle("我的Fitter");
			appear(mMeFragment);
			break;

		default:
			break;
		}
	}

	private void appear(Fragment fragment) {

		if (fragment == mShowingFragment) {
			return;
		}

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// if (mShowingFragment != null) {
		// transaction.hide(mShowingFragment);
		// } else {
		// transaction.hide(mHomeFragment);
		// }
		transaction.hide(mShowingFragment);
		transaction.show(fragment);
		mShowingFragment = fragment;
		transaction.commit();
	}

	@Override
	// tab bar 点击处理
	public void onItemClicked(int position, SwitchBarItemView itemView) {
		switchFragment(position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.red("main onActivityResult");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGOUT:
				L.red("logout");
				finish();
				skipTo(UserIdActivity.class);
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		L.red(MainActivity.class, "onSaveInstanceState");
		// super.onSaveInstanceState(outState);
		outState.remove("android:fragments");
		getFragmentManager().saveFragmentInstanceState(mHomeFragment);
		getFragmentManager().saveFragmentInstanceState(mScheduleFragment);
		getFragmentManager().saveFragmentInstanceState(mFriendsFragment);
		getFragmentManager().saveFragmentInstanceState(mMeFragment);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		L.red(MainActivity.class, "onRestoreInstanceState : "
				+ savedInstanceState);
		L.red(MainActivity.class,
				""
						+ getFragmentManager().getFragment(savedInstanceState,
								HomeFragment.class.getName()));
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		StatWrapper.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		StatWrapper.onPause(this);
	}
	
	static class StatWrapper {
		public static void onResume(Context context) {

			/**
			 * 此处调用基本统计代码
			 */
			StatService.onResume(context);
		}

		public static void onPause(Context context) {

			/**
			 * 此处调用基本统计代码
			 */
			StatService.onPause(context);
		}
	}
}
