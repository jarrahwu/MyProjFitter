package com.chzh.fitter.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;


/**
 * 简单的标题栏
 */
public class SimpleTitleBar extends BaseView {


	private Activity mDelegateActivity;

	public SimpleTitleBar(Context context) {
		super(context);
	}

	public SimpleTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.simple_title);
	}


	/**
	 * 添加View到标题的中间
	 * @param res
	 */
	public void addMiddleView(int res) {
		View v = mInflater.inflate(res, null);
		addMiddleView(v);
	}

	/**
	 * 添加View到标题的中间
	 * @param v
	 */
	public void addMiddleView(View v) {
		FrameLayout midFrameLayout = (FrameLayout) findView(R.id.title_mid_container);
		midFrameLayout.addView(v);
	}

	/**
	 * 添加View到标题右边
	 * @param res
	 */
	public void addRightSideView(int res) {
		View v = mInflater.inflate(res, null);
		addRightSideView(v);
	}

	/**
	 * 添加View到标题的右边
	 * @param v
	 */
	public void addRightSideView(View v) {
		FrameLayout rightSideFrameLayout = (FrameLayout) findView(R.id.title_right_container);
		rightSideFrameLayout.addView(v);
	}

	/**
	 * 获取返回的View
	 * @return
	 */
	public TextView getBackView() {
		return (TextView) findView(R.id.title_back);
	}

	/**
	 * 开启back按键结束Activity
	 * @param act
	 */
	public void enableOnBackFinish(Activity act) {
		mDelegateActivity = act;
		bindClickEvent(findView(R.id.title_back), "onTitleBackClicked");
	}

	public void onTitleBackClicked(View v) {
		if (mDelegateActivity != null) {
			mDelegateActivity.finish();
		}else {
			Log.e(this.getClass().toString(), "onTitleBackClicked delegate activity is null!");
		}
	}
}
