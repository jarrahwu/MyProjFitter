package com.chzh.fitter.framework;

import android.graphics.Color;

import com.chzh.fitter.view.SimpleTextTitleBar;

/**
 * 标题模版Activity基类
 */
public abstract class SimpleTitleSActivity extends BaseActivity{

	protected SimpleTextTitleBar mTitle;

	@Override
	protected void setupViews() {
		mTitle = new SimpleTextTitleBar(this);
		mTitle.setTitleText(getTitleName(), Color.WHITE, 18);
		mTitle.enableOnBackFinish(this);
		initInflater();

		setContentView(mTitle, getLayoutRes());
		setDefaultAppBackground();
		setupGUI();
	}

	/**
	 * 处理GUI逻辑
	 */
	protected  abstract void setupGUI();

	/**
	 * 标题名字
	 * @return
	 */
	protected abstract String getTitleName();

	/**
	 * layout
	 * @return
	 */
	protected abstract int getLayoutRes();

}
